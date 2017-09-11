package com.stone.app.dataBase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.realm.RealmObject;

public class MemberRelationData extends RealmObject {
    private String memberA;
    private String memberB;
    private int relation;

    public static final int DB_RELATION_AMOUNT = 7;
    public static final int DB_RELATION_UNKNOWN = 0;
    public static final int DB_RELATION_SPOUSE = 1;
    public static final int DB_RELATION_PARENT = 2;
    public static final int DB_RELATION_SIBLING = 3;
    public static final int DB_RELATION_DIVORCE = 4;
    public static final int DB_RELATION_ONESELF = 5;
    public static final int DB_RELATION_FRIEND = 6;

    public String getMemberA() {
        return memberA;
    }

    public String getMemberB() {
        return memberB;
    }

    public int getRelation() {
        return relation;
    }

    void setMemberA(String memberA) throws DataBaseError {
        Pattern p = Pattern.compile("\\D");
        Matcher m = p.matcher(memberA);
        if (m.find())
            throw new DataBaseError(DataBaseError.ErrorType.NotStandardID);
        this.memberA = memberA;
    }

    void setMemberB(String memberB) throws DataBaseError {
        Pattern p = Pattern.compile("\\D");
        Matcher m = p.matcher(memberB);
        if (m.find())
            throw new DataBaseError(DataBaseError.ErrorType.NotStandardID);
        this.memberB = memberB;
    }

    void setRelation(int relation) throws DataBaseError {
        if (relation < 0 || relation >= DB_RELATION_AMOUNT)
            throw new DataBaseError(DataBaseError.ErrorType.UnspecifiedRelation);
        this.relation = relation;
    }
}
