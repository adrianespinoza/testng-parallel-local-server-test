package com.itinvolve.itsm.framework.validators;

import com.itinvolve.itsm.framework.database.DatabaseOperation;
import com.itinvolve.itsm.framework.soql.BasicCondition;
import com.itinvolve.itsm.framework.soql.QueryBuilder;
import com.sforce.soap.partner.QueryResult;

public class StandardField {
    public String apiName;
    public SObejctEntity parentSObject;

    public boolean equals() {
        String query = getQuery();
        QueryResult queryResults = DatabaseOperation.query(query);

        boolean isEquals = false;
        if (queryResults.getSize() > 0) {
            isEquals = true;
        }
        return isEquals;
        //return false;
    }

    private String getQuery() {
        String query = QueryBuilder.select(apiName)
                            .from(parentSObject.apiName)
                            .where(BasicCondition.equals(apiName, parentSObject.valueComp.valueTo))
                            .orderBy("Id")
                            .toSoql();
        return query;
    }
}
