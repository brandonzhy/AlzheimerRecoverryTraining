package com.stone.app.dataBase;//package com.example.application.dataBase;
//
import io.realm.RealmObject;
import io.realm.annotations.Required;

public class MemberRelationData extends RealmObject {
    @Required
    private String memberA;
    @Required
    private String memberB;
    @Required
    private RELATION relation;

    enum RELATION{
        Spouse,
        ParentToChild,
        Sibling,
        PreSpouse
    }

    public String getMemberA(){
        return memberA;
    }

    public String getMemberB(){
        return memberB;
    }

    public RELATION getRelation(){
        return relation;
    }

    protected void setMemberA(String memberA){
        this.memberA = memberA;
    }

    protected void setMemberB(String memberB){
        this.memberB = memberB;
    }

    protected void setRelation(RELATION relation){
        this.relation = relation;
    }
}
