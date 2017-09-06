//package dataBase;
//
//import io.realm.RealmObject;
//import io.realm.annotations.Required;
//
//public class MemberRelationData extends RealmObject {
//    @Required
//    private String memberA;
//    @Required
//    private String memberB;
//    @Required
//    private RELATION relation;
//
//    enum RELATION{
//        HusbandToWife,
//        ParentToChild,
//        GrandParentToChild,
//        Sibling,
//        Other
//    }
//
//    public String getMemberA(){
//        return memberA;
//    }
//
//    public String getMemberB(){
//        return memberB;
//    }
//
//    public RELATION getRelation(){
//        return relation;
//    }
//
//    public void setMemberA(String memberA){
//        this.memberA = memberA;
//    }
//
//    public void setMemberB(String memberB){
//        this.memberB = memberB;
//    }
//
//    public void setRelation(RELATION relation){
//        this.relation = relation;
//    }
//}
