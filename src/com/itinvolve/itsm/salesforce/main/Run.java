package com.itinvolve.itsm.salesforce.main;


import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.print.attribute.standard.DateTimeAtCompleted;
import javax.xml.namespace.QName;

import org.apache.commons.lang3.StringUtils;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import com.itinvolve.itsm.framework.database.ApexDatabase;
import com.itinvolve.itsm.framework.database.ApexExecutor;
import com.itinvolve.itsm.framework.database.DataLoader;
import com.itinvolve.itsm.framework.database.DatabaseConnector;
import com.itinvolve.itsm.framework.database.DatabaseOperation;
import com.itinvolve.itsm.framework.database.DatabaseSchema;
import com.itinvolve.itsm.framework.database.MetadataUtils;
import com.itinvolve.itsm.framework.database.SObjectFactory;
import com.itinvolve.itsm.framework.database.SObjectUtils;
import com.itinvolve.itsm.framework.soql.AggregateFunction;
import com.itinvolve.itsm.framework.soql.BasicCondition;
import com.itinvolve.itsm.framework.soql.Columns;
import com.itinvolve.itsm.framework.soql.Condition;
import com.itinvolve.itsm.framework.soql.Function;
import com.itinvolve.itsm.framework.soql.LogicalOperator;
import com.itinvolve.itsm.framework.soql.Order;
import com.itinvolve.itsm.framework.soql.OrdersNull;
import com.itinvolve.itsm.framework.soql.QueryBuilder;
import com.itinvolve.itsm.framework.soql.QueryFactory;
import com.itinvolve.itsm.framework.soql.SetCondition;
import com.itinvolve.itsm.framework.validators.Comparator;
import com.itinvolve.itsm.framework.validators.Validator;
import com.sforce.async.BulkConnection;
import com.sforce.soap.apex.ExecuteAnonymousResult;
import com.sforce.soap.apex.SoapConnection;
import com.sforce.soap.partner.Connector;
import com.sforce.soap.partner.Error;
import com.sforce.soap.partner.Field;
import com.sforce.soap.partner.FieldType;
import com.sforce.soap.partner.GetUserInfoResult;
import com.sforce.soap.partner.LimitInfoHeader_element;
import com.sforce.soap.partner.LoginResult;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.soap.partner.QueryResult;
import com.sforce.soap.partner.SaveResult;
import com.sforce.soap.partner.sobject.SObject;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;

public class Run {
//    static final String USERNAME = "adriancito@aveh.com";
//    static final String PASSWORD = "Admin123";
//    static final String TOKEN = "5twfCIh8uR81BzKKmC6uSGxG9";
//    static String AUTHENDPOINT = "https://login.salesforce.com/services/Soap/u/29.0";
//    static String AUTHENDPOINT_SANDBOX = "https://test.salesforce.com/services/Soap/u/29.0";

//    static final String USERNAME = "adrian@jalasoft.com.adrian01";//"adriancito@aveh.com";
//    static final String PASSWORD = "Admin1234";//"Admin123";
//    static final String TOKEN = "QsB9ZENV3fP7sGicfJBRLDj1P";//"5twfCIh8uR81BzKKmC6uSGxG9";
//    static String AUTHENDPOINT = "https://login.salesforce.com/services/Soap/u/29.0";
//    static String AUTHENDPOINT_SANDBOX = "https://test.salesforce.com/services/Soap/u/29.0";

    static final String USERNAME = "adrian@jalasoft.com.rina17";//
    static final String PASSWORD = "Admin123";//"Admin123";
    static final String TOKEN = "";
    static String AUTHENDPOINT = "https://test.salesforce.com/services/Soap/u/29.0";

//    public static void executeApex (String apexCode) throws ConnectionException
//    {
//
//        String USERNAME = "adrian@jalasoft.com.adrian01";
//        String PASSWORD = "Admin1234QsB9ZENV3fP7sGicfJBRLDj1P";// + token
//        String AUTHENDPOINT = "https://test.salesforce.com/services/Soap/u/29.0";
//        //String AUTHENDPOINT = "https://cs9.salesforce.com/services/Soap/s/29.0";
//
//        ConnectorConfig config = new ConnectorConfig();
//        config.setUsername(USERNAME);
//        config.setPassword(PASSWORD);
//        config.setAuthEndpoint(AUTHENDPOINT);
//
//        PartnerConnection partnerConnection = Connector.newConnection(config);
//
//        System.out.println("Auth EndPoint: "+config.getAuthEndpoint());
//        System.out.println("Service EndPoint: "+config.getServiceEndpoint());
//        System.out.println("Username: "+config.getUsername());
//        System.out.println("SessionId: "+config.getSessionId());
//
//        ConnectorConfig soapConfig = new ConnectorConfig();
//        //String loginUrl = "http://login.salesforce.com";
//        String loginUrl = config.getServiceEndpoint().replace("//cs10", "//cs9").replace("/Soap/u/", "/Soap/s/");
//        System.out.println("---" + loginUrl);
//
//        soapConfig.setAuthEndpoint(loginUrl);
//        soapConfig.setServiceEndpoint(loginUrl);
//        soapConfig.setManualLogin(true);
//        soapConfig.setAuthEndpoint(config.getAuthEndpoint());
//        soapConfig.setServiceEndpoint(config.getServiceEndpoint().replace("//cs10", "//cs9").replace("/Soap/u/", "/Soap/s/"));//(config.getServiceEndpoint());
//        soapConfig.setSessionId(config.getSessionId());
//
//        SoapConnection connection = new SoapConnection(soapConfig);
//        ExecuteAnonymousResult result = connection.executeAnonymous(apexCode);
//        if (result.isSuccess()) {
//            System.out.println("hola si funciona!");
//        }
//    }


    public static ConnectorConfig getConnectorConfig (String username, String password) throws ConnectionException
    {
        // this can be changed to "http://login.salesforce.com" if you are using a production sandbox
        //String AUTHENDPOINT = "https://login.salesforce.com/services/Soap/u/29.0";
        //String AUTHENDPOINT = "https://login.salesforce.com/services/Soap/u/29.0";
        String AUTHENDPOINT = "https://test.salesforce.com/services/Soap/u/29.0";
        //String loginUrl = "http://test.salesforce.com";

//        final ConnectorConfig config = new ConnectorConfig();
//        config.setAuthEndpoint(AUTHENDPOINT);
//        config.setServiceEndpoint(AUTHENDPOINT);
//        config.setManualLogin(true);
//        LoginResult result = null;
//        try {
//            result = (new PartnerConnection(config)).login(username, password);
//        } catch (ConnectionException e) {
//            e.printStackTrace();
//        }

        ConnectorConfig config = new ConnectorConfig();
        config.setUsername(username);
        config.setPassword(password);
        config.setAuthEndpoint(AUTHENDPOINT);

        PartnerConnection partnerConnection = Connector.newConnection(config);

        System.out.println("Auth EndPoint: "+config.getAuthEndpoint());
        System.out.println("Service EndPoint: "+config.getServiceEndpoint());
        System.out.println("Username: "+config.getUsername());
        System.out.println("SessionId: "+config.getSessionId());

        // now we need to create a new connection to the server URL returned by the login response
        if (partnerConnection != null)
        {
            ConnectorConfig soapConfig = new ConnectorConfig();

            // do NOT use the "test.salesforce.com" URL here - it won't work
            System.out.println("--- " + config.getServiceEndpoint());
//            config2.setAuthEndpoint(result.getServerUrl());
//            config2.setServiceEndpoint(result.getServerUrl());
//            config2.setSessionId(result.getSessionId());
            soapConfig.setAuthEndpoint(config.getAuthEndpoint());
            soapConfig.setServiceEndpoint(config.getServiceEndpoint().replace("/Soap/u/", "/Soap/s/"));
            soapConfig.setSessionId(config.getSessionId());
            return soapConfig;
        }
        else
        {
            return null;
        }
    }


    public static void executeApex (String apexCode)
    {
        SoapConnection conn;
        try {
//            String USERNAME = "adriancito@aveh.com";
//            String PASSWORD = "Admin1235twfCIh8uR81BzKKmC6uSGxG9";// + token
            String USERNAME = "adrian@jalasoft.com.rina17";
            String PASSWORD = "Admin123";// + token
            conn = new SoapConnection(getConnectorConfig(USERNAME, PASSWORD));
            ExecuteAnonymousResult result = conn.executeAnonymous(apexCode);
            System.out.println("result: " + result);
        } catch (ConnectionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static String quote(String value) {
        if (value == null) {
            return "null";
        }
        StringBuffer str = new StringBuffer();
        str.append('\'');
        for (int i = 0; i < value.length(); i++) {
            if ((value.charAt(i) == '\\') || (value.charAt(i) == '\"') || (value.charAt(i) == '\'')) {
                str.append('\\');
            }
            str.append(value.charAt(i));
        }
        str.append('\'');
        return str.toString();
    }

    public static void main(String[] args) throws ParseException {
        //System.out.println(SObjectUtils.getRealValueApex("Computer__c", "Business_Owner__c", "kasdfjalfjlafjsdkl"));

//solving issue partner WSDL
        DatabaseConnector.connect(USERNAME, PASSWORD, TOKEN, AUTHENDPOINT);

        ApexDatabase.update("Computer__c", "a0eK0000002T5JnIAK", "Business_Owner__c", "");
//
//        /**ISSUE Can Not Be Deleted: Blocking Relationships Are Present. FIELD_CUSTOM_VALIDATION_EXCEPTION*/
//
//        QueryBuilder query = QueryBuilder.select("cmcloud__Business_Owner__c", "cmcloud__IT_Asset__c").from("cmcloud__Computer__c").where(BasicCondition.equals("Id", "a0eK0000002T4fEIAS"));
//
//        ApexExecutor.executeApex("SObject so = [" + query.toSoql() + "]; " +
//                "system.debug('cmcloud__IT_Asset__c: ' + so.get('cmcloud__IT_Asset__c'));" +
//                "so.put('cmcloud__Business_Owner__c', '');" +
//                "update new List<SObject>{so};");

//        QueryBuilder q = QueryBuilder.select(AggregateFunction.count()).from("Book__c");
//
//        int num = DatabaseSchema.countRecords("Book__c");
//
//        System.out.println("count: " + num);
//
//        //SObject last = DatabaseSchema.getLastRecord("Book__c");
//        SObject last = DatabaseSchema.getLastRecordModified("Book__c");
//
//        System.out.println("last id: " + last.getId());
//        System.out.println("get value: " + SObjectUtils.getValueFromRecord(last, "Author__c"));
//        SObjectUtils.setValueToRecord(last, "FirstDate__c", "2013-10-15");
//
//        DatabaseOperation.update(new SObject[]{last});
//
//        QueryBuilder query = QueryBuilder.select(Columns.ALL).from("Book__c");
//        QueryResult res = DatabaseOperation.query(query);
//        System.out.println("-----: " + res.getSize());


        /**SOME COMPUTERS FIELS ARE NOT UPDATABLES*/
//        QueryBuilder query = QueryBuilder.select(MetadataUtils.getFieldNamesList("Computer__c")).from("Computer__c").where(BasicCondition.equals("Id", "a0eK0000002T3HhIAK"));
//        System.out.println("query: " + query.toSoql());
//
//        QueryResult res = DatabaseOperation.query(query);
//        SObject[] records = res.getRecords();
//        DatabaseOperation.update(records);

//        String id = null;
//        String id1 = "a0eK0000002T3e9IAC";
//        String[] ids = new String[2];
//        ids[0] = id;
//        ids[1] = id1;
//        DatabaseOperation.delete(ids);


        /** ISSUE DO NOT UPDATE DATE FIELDS */
//        QueryResult qResult = DatabaseOperation.query("SELECT Id, Cpny__Author__c,Cpny__BookPhone__c,Cpny__FirstDateTime__c,Cpny__FirstDate__c,Cpny__IsImported__c,Cpny__Library__c,Cpny__Pages__c,Cpny__Price__c,Cpny__Title__c,Cpny__Use__c FROM Cpny__Book__c");
//
//        SObject[] records = qResult.getRecords();
//
//        System.out.println("Records: " + records.length);
//
//        //DatabaseOperation.delete(records);
//
//        for (int i = 0; i < records.length; i++) {
//            records[i].setField("Cpny__Author__c", "ANONIMO 2");
//            //records[i].setField("Cpny__IsImported__c", false);
//        }
//        DatabaseOperation.update(records);


        /**COMPUTER DELETE ISSUE FIELD_CUSTOM_VALIDATION_EXCEPTION*/

//        QueryBuilder query = QueryBuilder.select("Business_Owner__c","IT_Asset__c","Id","Name").
//            from("Computer__c").
//            where(BasicCondition.equals("Id", "a0eK0000002T4adIAC"));
//        QueryResult res = DatabaseOperation.query(query);
//
//        SObject[] sobjs = res.getRecords();
//
//        for (int i = 0; i < sobjs.length; i++) {
//            System.out.println("Name " + sobjs[i].getField("Name"));
//            sobjs[i].setField("cmcloud__Business_Owner__c", null);
//            System.out.println("SObject:: " + sobjs[i]);
//        }
//        DatabaseOperation.update(sobjs);
        //DatabaseOperation.delete(sobjs);

        /** METADATA ISSUE DON'T LOAD USER FIELDS */
//        String[] fields = MetadataUtils.getFieldTypesMap("Book__c").keySet().toArray(new String[] {});
//        for (int i = 0; i < fields.length; i++) {
//            System.out.println("Field: " + fields[i]);
//        }

        /**DELETE RECORDS BY OBJECT TYPE*/
        //DatabaseOperation.deleteAllByObjectType("Book__c");
        /**DELETE RECORDS BY QUERY*/

        //QueryBuilder query = QueryBuilder.select("Name").from("Book__c"); // Correct
        //QueryBuilder query = QueryBuilder.select("Name").from("Book__c").where(LogicalOperator.and(BasicCondition.equals("Author__c", "adrian"), BasicCondition.equals("Title__c", "EA"))); // Correct with filter
        //QueryBuilder query = QueryFactory.countFn("Book__c"); // DML operation DELETE not allowed on AggregateResult
        //QueryBuilder query = QueryBuilder.select("Name").from("Book__c").groupBy("Id"); // DML operation DELETE not allowed on AggregateResult
        //DatabaseOperation.delete(query);

        /**DATALOADER ISSUE type values ---- path is not allowed in file name*/
        //List<String> ids = DataLoader.loadData("Book__c", "testfolder\\Books.csv");
//        List<SObject> sObjectRes = DataLoader.loadSObjectData("Book__c", "testfolder\\Books.csv");
//        for (SObject sObject : sObjectRes) {
//            System.out.println(sObject.getField("Cpny__Author__c"));
//            System.out.println(sObject.getId());
//        }
//        for (String id : ids) {
//            System.out.println(id);
//        }

        /**PARTNER WSDL ISSUE*/
//        int cant = 250;
//        SObject[] objToInsert = new SObject[cant];
//        for (int i = 0; i < cant; i++) {
//            SObject obj = SObjectUtils.createSObject("Library__c");
//            obj.setField("Name", "Library" + i);
//            objToInsert[i] = obj;
//        }
//
//        SaveResult[] saveResults = DatabaseOperation.insert(objToInsert);


//        String[] ids = new String[saveResults.length];
//        for (int i=0; i< saveResults.length; i++) {
//            if (saveResults[i].isSuccess()) {
//                ids[i] = saveResults[i].getId();
//            }
//        }
//
//        DatabaseOperation.delete(ids);

//        QueryBuilder query = QueryBuilder.select("Id", "Name", "Location__c").from("Library__c");
//
//        QueryResult qResult = DatabaseOperation.query(query.toSoql());
//
//        SObject[] records = qResult.getRecords();
//
//        System.out.println("Records: " + records.length);
//
//        //DatabaseOperation.delete(records);
//
//        for (int i = 0; i < records.length; i++) {
//            records[i].setField("Cpny__Location__c", "El Prado");
//        }
//
//        DatabaseOperation.update(records);


//        Field[] fields = MetadataUtils.getSObjectFieldsDescribe("Cpny__book__c");//Cpny__book__c, cmcloud__Computer__c
//        for (int i = 0; i < fields.length; i++) {
//            System.out.println(fields[i].getName() + " - Type - " + fields[i].getType().equals(FieldType._double));
//        }

        DatabaseConnector.disconnect();


        /**VALIDATORS SAMPLES*/
//        DatabaseConnector.connect(USERNAME, PASSWORD, TOKEN, AUTHENDPOINT);
////        Query q = Query.select(AggregateFunction.count("Id"))
////                        .from("Book__c");
////        String query = q.toSoql();
////        System.out.println("Query as string " + query);
////
////        boolean resCompare = Validator.countCompareTo(q, Comparator.LESS_THAN, 2);
////        if (resCompare) {
////            System.out.println("Are equals");
////        } else {
////            System.out.println("Are not equals");
////        }
//
//        BasicCondition basicCond = BasicCondition.equals("Name", "Adrian");
//        //boolean resCompare2 = Validator.countCompareTo(QueryFactory.aggregateFunctionQuery(Function.COUNT, "Book__c", basicCond), Comparator.EQUALS, 1);
//        //boolean resCompare2 = Validator.countCompareTo(QueryFactory.aggregateFunctionQuery(Function.COUNT, "Cpny__Title__c", "Book__c"), Comparator.EQUALS, 2);
//
//
//        boolean resCompare2 = Validator.maxCompareTo(QueryFactory.maxFn("Book__c", "Cpny__Price__c"), Comparator.EQUALS, 450);
//        if (resCompare2) {
//            System.out.println("-> Test: Are equals");
//        } else {
//            System.out.println("-> Test: Are not equals");
//        }
//
//        /*QueryResult qr = DatabaseOperation.query(query);
//        System.out.println("Value: " + Comparator.EQUALS.ordinal());
//
//        System.out.println("Query result: " + qr);//.getRecords()[0].getField("customAlias"));*/
//        DatabaseConnector.disconnect();


        /*String query1 = Query.select("Library__c", "Author__c", "Title__c")
                            .from("Book__c")
                            .where(BasicCondition.equals("Author__c", "La merced"))
                            .orderBy("Author__c", Order.ASC, OrdersNull.NULLS_FIRST)
                            .toSoql();

        System.out.println("query1: \n" + query1);

        String query12 = Query.select("Author__c", AggregateFunction.avg("Id"))
                .from("Book__c")
                .where(BasicCondition.equals("Author__c", "La merced"))
                .groupBy("Author__c")
                .toSoql();

        System.out.println("query1: \n" + query12);


        String query2 = Query.select("Library__c, Author__c, Title__c")
                            .from("Cpny__Book__c")
                            .where(SetCondition.in("Library__c", new String[] { "post", "fedex", "goat" }))
                            .toSoql();

        System.out.println("query2: \n" + query2);*/


        /*Query subquery = Query.select("Id")
                                .from("Library__c")
                                .where(BasicCondition.equals("Name", "El deber"));

        String query21 = Query.select("Library__c, Author__c, Title__c")
            .from("Cpny__Book__c")
            .where(SetCondition.in("Library__c", subquery))
            .toSoql();
        System.out.println("query2: \n" + query21);*/
        /*Condition name = BasicCondition.like("Name", "a%");
        Condition id = BasicCondition.equals("id", 12345);
        Condition feet = BasicCondition.equals("Author__c", "smelly");

        String query3 = Query.select("Library__c, Author__c, Title__c")
                            .from("Book__c")
                            .where(LogicalOperator.or(name, feet))
                            .toSoql();

        System.out.println("query3: \n" + query3);

        String query4 = Query.select("Library__c, Author__c, Title__c")
                .from("Cpny__Book__c")
                .where(LogicalOperator.or(name, LogicalOperator.and(id, feet)))
                .toSoql();

        System.out.println("query4: \n" + query4);*/

        /*DatabaseConnector.connect(USERNAME, PASSWORD, TOKEN, AUTHENDPOINT);

        DataLoader.loadData("Cpny__Book__c", "Books.csv");
        //boolean isEquals = Validator.validate("Smith").in("Cpny__Book__c").withStandardField("Author__c").equals();

        boolean isEquals = Validator.compare("La merced").inSObject("Cpny__Book__c").withReferenceField("Library__c").withStandardField("Name").equals();

        if (isEquals) {
            System.out.println("The data is correct!");
        } else {
            System.out.println("The data is not correct!");
        }/**/

        /*Query query = Query.select("Library__r.Name")
                    .from("Book__c")
                    .where(BasicCondition.equals("Library__r.Name", "La merced"))
                    .orderBy("Id");

        boolean isEmpty = Validator.isEmpty(query);

        if (isEmpty) {
            System.out.println("The query result is empty!");
        } else {
            System.out.println("The query result is not empty!");
        }*/
        //DatabaseConnector.disconnect();

        //SObject[] res = SObjectFactory.buildSObjects("StagingListPrice__c", "ListPrice.csv");
        /*SObject[] res = SObjectFactory.buildSObjects("Cpny__Book__c", "Books.csv");

        for (int i = 0; i < res.length; i++) {
            System.out.println("P field: " + res[i].getField("Author__c"));
        }*/

        //CsvFileReader.readFile1("ListPrice.csv");
    }

    public static <T> T[] convertToArray(List<?> list, Class<T> c) {
        @SuppressWarnings("unchecked")
        T[] result = (T[]) Array.newInstance(c, list.size());
        result = list.toArray(result);
        return (T[]) result;
    }

    static <T> T[] append(T[] arr, T element) {
        final int N = arr.length;
        arr = Arrays.copyOf(arr, N + 1);
        arr[N] = element;
        return arr;
    }
}

