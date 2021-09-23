package com.online.assignment.camelbootservice2.routes;

import org.apache.camel.CamelContext;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.dataformat.BindyType;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.online.assignment.camelbootservice2.models.User;
import com.online.assignment.camelbootservice2.models.bindy.UserDataCsvRecords;
import com.online.assignment.camelbootservice2.service.UserDeEncryptionData;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import static org.apache.camel.Exchange.CONTENT_TYPE;
import static org.apache.camel.Exchange.HTTP_RESPONSE_CODE;
import static org.apache.camel.model.rest.RestParamType.path;

import static com.online.assignment.camelbootservice2.constants.UserConstants.*;

@Component("userExposeRoute")
public class UserExposeRoute extends RouteBuilder {

    @Autowired
    UserDeEncryptionData userDeEncryptionData;

    @Value("${rest.api.base.url:/com/user/service2/v1}")
    private String restApiBaseUrl;

    @Value("${rest.api.version:1.0}")
    private String restApiVersion;

    @Value("${rest.api.title}")
    private String restApiTitle;

    @Value("${rest.api.description}")
    private String restApiDesc;

    @Value("${swagger.url:/apidocs}")
    private String swaggerUrl;

    @Value("${rest.api.host}")
    private String restApiHost;

    @Value("${user.get.internal.resource.path:/getUserDetails}")
    private String getUserResourcePath;

    @Value("${user.update.internal.resource.path:/updateUserDetails}")
    private String updateUserResourcePath;

    @Override
    public void configure() throws Exception {
        CamelContext context = new DefaultCamelContext();

            restConfiguration()
                .component("servlet")
                .bindingMode(RestBindingMode.json)
                .dataFormatProperty("prettyPrint", "true")
                .dataFormatProperty("json.in.disableFeatures", "FAIL_ON_UNKNOWN_PROPERTIES")
                .host(restApiHost)
                .contextPath(restApiBaseUrl)
                .apiContextPath(swaggerUrl)
                .apiProperty("api.description", restApiDesc)
                .apiProperty("api.title", restApiTitle)
                .apiProperty("api.version", restApiVersion)
                .apiProperty("cors", "true")
                .setSkipBindingOnErrorCode(true);

        // Get/Create/Update User Info from file
        rest().description("Get User Internal Info REST Service")
            .consumes(APPLICATION_JSON).produces(APPLICATION_JSON)

            .get(getUserResourcePath + "?" +  USER_ID + "={" + USER_ID + "}")
            .produces(APPLICATION_JSON)
            .responseMessage().code(200).message("The Get User Details model").responseModel(User.class).endResponseMessage()
            .description("Get User details from file")
            .outType(User.class)
            .to("direct:getUserDetails")

            .put(updateUserResourcePath + "?" +  FILE_TYPE + "={" + FILE_TYPE + "}")
            .consumes(APPLICATION_JSON).produces(APPLICATION_JSON)
            .param().name(FILE_TYPE).type(path).description("Input File Type").dataType("string").endParam()
            .responseMessage().code(200).message("Update User Response model").responseModel(User.class).endResponseMessage()
            .description("Update User details to file")
            .outType(User.class)
            .to("direct:updateUserDetails");

        from("direct:getUserDetails")
            .routeId(GET_USER_ROUTE_ID)
            .onCompletion()
                .log(LoggingLevel.DEBUG, "Get User Details request execution completed.")
                .id(ROUTE_END)
            .end()
            .log(LoggingLevel.INFO, "Get User Details request received ")
            .bean("userFileProcessor","readUserFile")
            .removeHeaders("*", HTTP_RESPONSE_CODE, CONTENT_TYPE);

        from("activemq:user-encryptor-active-mq")
            .routeId(CREATE_USER_ROUTE_ID)
            .onCompletion()
                .log(LoggingLevel.DEBUG, "Create User Details request execution completed.")
                .id(ROUTE_END)
            .end()
            .log(LoggingLevel.INFO, "Create User Details request received for File Type : ${headers." + FILE_TYPE + "}")
            .log("${body}")
            .unmarshal(userDeEncryptionData.createEncryptor())
            .log("${body}")
            .unmarshal().json(JsonLibrary.Jackson, User.class)
            .choice()
                .when(simple("${headers.fileType} == 'XML'"))
                    .log("XML FILE")
                    .to("file:files/output?fileName=userdata.xml")
                .otherwise()
                    .log("CSV FILE")
                    .bean("userFileProcessor", "convertResponseObjectToBindyFormat")
                    .marshal()
                        .bindy(BindyType.Csv, UserDataCsvRecords.class)
                    .to("file:files/output?fileName=userdata.csv")
            .end()
            .removeHeaders("*", HTTP_RESPONSE_CODE, CONTENT_TYPE);

        from("direct:updateUserDetails")
            .routeId(UPDATE_USER_ROUTE_ID)
            .onCompletion()
                .log(LoggingLevel.DEBUG, "Update User Details request execution completed.")
                .id(ROUTE_END)
            .end()
            .log(LoggingLevel.INFO, "Update User Details request received for File Type : ${headers." + FILE_TYPE + "}")
            .log("${body}")
            .unmarshal(userDeEncryptionData.createEncryptor())
            .log("${body}")
            .transform().body(String.class)
            .choice()
                .when(simple("${headers.fileType} == 'XML'"))
                    .log("XML FILE")
                    .to("file:files/output?fileName=userdata.xml")
                .otherwise()
                    .log("CSV FILE").marshal().csv()
                    .to("file:files/output?fileName=userdata.csv")
            .end()
            .removeHeaders("*", HTTP_RESPONSE_CODE, CONTENT_TYPE);

        }
}
