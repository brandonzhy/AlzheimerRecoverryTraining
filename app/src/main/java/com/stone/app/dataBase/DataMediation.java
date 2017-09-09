package com.stone.app.dataBase;

import android.util.Log;

import com.stone.app.dataBase.DataBaseManager.RelationRadiationChart;
import com.stone.app.dataBase.DataBaseManager.RelationStrengthIndex;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

class DataMediation {
    // Record relation list
    private static final class MARROW {
        String Other; // ID of the other member
        int Relation;
        boolean CIS; // whether the relation is cis- / trans-
    }

    private void PrimaryMemberIDCheck(Realm Manager, String memberID) throws DataBaseError {
        RealmResults<MemberData> m = Manager.where(MemberData.class).equalTo("ID", memberID).findAll();
        if(m.size() <= 0) {
            Log.i("TAG", "PrimaryMemberIDCheck : MemberNotExist");
            throw new DataBaseError(DataBaseError.ErrorType.MemberNotExist);
        }
        else if(m.size() > 1) {
            Log.i("TAG", "PrimaryMemberIDCheck : MemberRedundant");
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_MemberData);
        }
        else if(!m.first().getActivate()) {
            Log.i("TAG", "PrimaryMemberIDCheck : MemberHibernating");
            throw new DataBaseError(DataBaseError.ErrorType.MemberHibernating);
        }
    }

    private void PrimaryFamilyIDCheck(Realm Manager, String familyID) throws DataBaseError {
        RealmResults<FamilyData> f = Manager.where(FamilyData.class).equalTo("ID", familyID).findAll();
        if(f.size() <= 0) {
            Log.i("TAG", "PrimaryFamilyIDCheck: FamilyNotExist");
            throw new DataBaseError(DataBaseError.ErrorType.FamilyNotExist);
        }
        else if(f.size() > 1) {
            Log.i("TAG", "PrimaryFamilyIDCheck : FamilyRedundant");
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_FamilyData);
        }
        else if(!f.first().getActivate()) {
            Log.i("TAG", "PrimaryFamilyIDCheck : FamilyHibernating");
            throw new DataBaseError(DataBaseError.ErrorType.FamilyHibernating);
        }
    }

    private void PrimaryRecordIDCheck(Realm Manager, String recordID) throws DataBaseError {
        RealmResults<GameRecordData> r = Manager.where(GameRecordData.class).equalTo("recordID", recordID).findAll();
        if(r.size() <= 0) {
            Log.i("TAG", "PrimaryRecordIDCheck : RecordNotExist");
            throw new DataBaseError(DataBaseError.ErrorType.RecordNotExist);
        }
        else if(r.size() > 1) {
            Log.i("TAG", "PrimaryRecordIDCheck : GameRecordRedundant");
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_GameRecordData);
        }
        else if(!r.first().getActivate()) {
            Log.i("TAG", "PrimaryRecordIDCheck : RecordHibernating");
            throw new DataBaseError(DataBaseError.ErrorType.RecordHibernating);
        }
    }

    private void PrimaryPictureIDCheck(Realm Manager, String pictureID) throws DataBaseError {
        RealmResults<PictureData> p = Manager.where(PictureData.class).equalTo("ID", pictureID).findAll();
        if(p.size() <= 0) {
            Log.i("TAG", "PrimaryPictureIDCheck : ImageNotExist");
            throw new DataBaseError(DataBaseError.ErrorType.ImageNotExist);
        }
        else if(p.size() > 1) {
            Log.i("TAG", "PrimaryPictureIDCheck : PictureRedundant");
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_PictureData);
        }
        else if(!p.first().getActivate()) {
            Log.i("TAG", "PrimaryPictureIDCheck : PictureHibernating");
            throw new DataBaseError(DataBaseError.ErrorType.PictureHibernating);
        }
    }

    // Migrate relation on B to A
    private void AutoTransplanting(Realm Manager, String memberA, String memberB)
            throws DataBaseError, DataBaseSignal{
        try{
            // Combine original family
            MemberData memA = getMemberList(Manager, memberA,"","","").get(0);
            MemberData memB = getMemberList(Manager, memberB,"","","").get(0);
            if(!memA.getFamilyID().equals(memB.getFamilyID())) {
                try {
                    AddExistMemberToExistFamily(Manager, memberB, memA.getFamilyID());
                } catch (DataBaseSignal s) {
                    if(DataBaseSignal.SignalType.AddSingleMemberToFamilySucceed != s.getSignalType()
                            && DataBaseSignal.SignalType.MergeTwoFamiliesSucceed != s.getSignalType()) {
                        Log.i("TAG", "AutoTransplanting : " + s.getSignalType() + s.getMessage() + s.getLocalizedMessage());
                        throw s;
                    }
                }
            }

            // Transplant all the relations on B to A
            RealmResults<MemberRelationData> Result = Manager.where(MemberRelationData.class)
                    .equalTo("memberA",memberB).or().equalTo("memberB",memberB).findAll();
            MemberRelationData m;
            boolean autoTransplant = false;
            int telomere = Result.size();
            while(Result.size() > 0 && telomere-- >= 0){
                m = Result.last();
                // Check relation between A and B
                if(memberA.equals(m.getMemberA()) || memberA.equals(m.getMemberB())){
                    if(MemberRelationData.DB_RELATION_ONESELF != m.getRelation()){
                        Log.i("TAG", "AutoTransplanting : RelationError_AlloTransplanting");
                        throw new DataBaseError(DataBaseError.ErrorType.RelationError_AlloTransplanting);
                    }
                    else{
                        autoTransplant = true;
                        try {
                            DestroyRelation(Manager, memberA, memberB);
                        } catch (DataBaseSignal s){
                            if(DataBaseSignal.SignalType.MemberRelationDeleted != s.getSignalType()) {
                                Log.i("TAG", "AutoTransplanting : " + s.getSignalType() + s.getMessage() + s.getLocalizedMessage());
                                throw s;
                            }
                        }
                    }
                }
                // Relation "(memberB, member)" is to transplant
                else if(memberB.equals(m.getMemberA())){
                    String member = m.getMemberB();
                    int relation = m.getRelation();
                    try {
                        DestroyRelation(Manager, memberB, member);
                    } catch (DataBaseSignal s){
                        if(DataBaseSignal.SignalType.MemberRelationDeleted != s.getSignalType()) {
                            Log.i("TAG", "AutoTransplanting : " + s.getSignalType() + s.getMessage() + s.getLocalizedMessage());
                            throw s;
                        }
                    }
                    List<MemberRelationData> list = getMemberRelationList(Manager, member, relation);
                    int telomere_A = list.size();
                    for(int i=list.size()-1;i>=0;i--){
                        if(list.size() != telomere_A) {
                            Log.i("TAG", "AutoTransplanting : RealmListExceptionallyUpdated");
                            throw new DataBaseError(DataBaseError.ErrorType.RealmListExceptionallyUpdated);
                        }
                        if(memberA.equals(list.get(i).getMemberA()) || memberA.equals(list.get(i).getMemberB()))
                            break;
                        if(0 == i){
                            try{
                                AddMemberRelation(Manager, memberA, member, relation);
                            } catch (DataBaseSignal s){
                                if(DataBaseSignal.SignalType.MemberRelationAddedAlready != s.getSignalType()) {
                                    Log.i("TAG", "AutoTransplanting : " + s.getSignalType() + s.getMessage() + s.getLocalizedMessage());
                                    throw s;
                                }
                            }
                        }
                    }
                }
                // Relation "(member, memberB)" is to transplant
                else {
                    String member = m.getMemberA();
                    int relation = m.getRelation();
                    try {
                        DestroyRelation(Manager, member, memberB);
                    } catch (DataBaseSignal s){
                        if(DataBaseSignal.SignalType.MemberRelationDeleted != s.getSignalType()) {
                            Log.i("TAG", "AutoTransplanting : " + s.getSignalType() + s.getMessage() + s.getLocalizedMessage());
                            throw s;
                        }
                    }
                    List<MemberRelationData> list = getMemberRelationList(Manager, member, relation);
                    int telomere_T = list.size();
                    for(int i=list.size()-1;i>=0;i--){
                        if(list.size() != telomere_T) {
                            Log.i("TAG", "AutoTransplanting : RealmListExceptionallyUpdated");
                            throw new DataBaseError(DataBaseError.ErrorType.RealmListExceptionallyUpdated);
                        }
                        if(memberA.equals(list.get(i).getMemberA()) || memberA.equals(list.get(i).getMemberB()))
                            break;
                        if(0 == i){
                            try{
                                AddMemberRelation(Manager, member, memberA, relation);
                            } catch (DataBaseSignal s){
                                if(DataBaseSignal.SignalType.MemberRelationAddedAlready != s.getSignalType()) {
                                    Log.i("TAG", "AutoTransplanting : " + s.getSignalType() + s.getMessage() + s.getLocalizedMessage());
                                    throw s;
                                }
                            }
                        }
                    }
                }
            }
            if(Result.size() > 0) {
                Log.i("TAG", "AutoTransplanting : RealmResultAutoUpdateFail");
                throw new DataBaseError(DataBaseError.ErrorType.RealmResultAutoUpdateFail);
            }

            // Re-construct oneself relation
            if(!autoTransplant) {
                Log.i("TAG", "AutoTransplanting : RelationError_UnknownTransplanting");
                throw new DataBaseError(DataBaseError.ErrorType.RelationError_UnknownTransplanting);
            }
            else{
                try{
                    AddMemberRelation(Manager, memberA, memberB, MemberRelationData.DB_RELATION_ONESELF);
                } catch(DataBaseSignal s) {
                    if(DataBaseSignal.SignalType.MemberRelationAddedAlready == s.getSignalType()) {
                        Log.i("TAG", "AutoTransplanting : " + s.getSignalType() + s.getMessage() + s.getLocalizedMessage());
                        throw new DataBaseSignal(DataBaseSignal.SignalType.AutoTransplantationSucceed);
                    }
                }
            }
        } catch (DataBaseError | DataBaseSignal e) {
            throw e;
        } catch (Exception e) {
            Log.i("TAG", "AutoTransplanting : " + e.getMessage() + e.getLocalizedMessage());
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_AutoTransplanting);
        }
    }

    // Requires healthy tree
    private String FindSiblingListHead(Realm Manager, String member) throws DataBaseError {
        try {
            List<MemberRelationData> siblingList = getMemberRelationList(Manager, member, MemberRelationData.DB_RELATION_SIBLING);
            MemberRelationData relation;
            int telomere_C = siblingList.size();
            for (int i = 0; i < siblingList.size(); i++) {
                if(siblingList.size() != telomere_C) {
                    Log.i("TAG", "FindSiblingListHead : RealmListExceptionallyUpdated");
                    throw new DataBaseError(DataBaseError.ErrorType.RealmListExceptionallyUpdated);
                }
                relation = siblingList.get(i);
                if (member.equals(relation.getMemberB()))
                    return FindSiblingListHead(Manager, relation.getMemberA());
            }
            return member;
        } catch (DataBaseError e) {
            throw e;
        } catch (Exception e) {
            Log.i("TAG", "FindSiblingListHead : " + e.getMessage() + e.getLocalizedMessage());
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_FindSiblingListHead);
        }
    }

    // Requires healthy tree
    private String FindSiblingListTail(Realm Manager, String member) throws DataBaseError {
        try {
            List<MemberRelationData> siblingList = getMemberRelationList(Manager, member, MemberRelationData.DB_RELATION_SIBLING);
            MemberRelationData relation;
            int telomere_G = siblingList.size();
            for (int i = 0; i < siblingList.size(); i++) {
                if(siblingList.size() != telomere_G) {
                    Log.i("TAG", "FindSiblingListTail : RealmListExceptionallyUpdated");
                    throw new DataBaseError(DataBaseError.ErrorType.RealmListExceptionallyUpdated);
                }
                relation = siblingList.get(i);
                if (member.equals(relation.getMemberA()))
                    return FindSiblingListTail(Manager, relation.getMemberB());
            }
            return member;
        } catch (DataBaseError e) {
            throw e;
        } catch (Exception e) {
            Log.i("TAG", "FindSiblingListTail : " + e.getMessage() + e.getLocalizedMessage());
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_FindSiblingListTail);
        }
    }

    // Recursively find the path from A to B
    private List<MARROW> MarrowPuncture(Realm Manager, String memberA, String memberB, List<MARROW> marrow) throws DataBaseError {
        try {
            if(null == marrow) marrow = new ArrayList<>(1);
            List<MemberRelationData> list = getMemberRelationList(Manager, memberA, 0);
            int telomere = list.size();
            MemberRelationData r;

            // First find if B is neighbour to A now
            for(int i=0;i<list.size();i++){
                r = list.get(i);
                if(telomere != list.size()){
                    Log.i("TAG", "MarrowPuncture : RealmListExceptionallyUpdated");
                    throw new DataBaseError(DataBaseError.ErrorType.RealmListExceptionallyUpdated);
                }
                else if(memberB.equals(r.getMemberA())) {
                    MARROW newMarrow = new MARROW();
                    newMarrow.Other = memberB;
                    newMarrow.Relation = r.getRelation();
                    newMarrow.CIS = false;
                    marrow.add(newMarrow);
                    return marrow;
                }
                else if(memberB.equals(r.getMemberB())) {
                    MARROW newMarrow = new MARROW();
                    newMarrow.Other = memberB;
                    newMarrow.Relation = r.getRelation();
                    newMarrow.CIS = true;
                    marrow.add(newMarrow);
                    return marrow;
                }
            }

            // Recursively search for A's neighbourhood
            for(int i=0;i<list.size();i++) {
                r = list.get(i);
                String member;
                boolean cis;
                // Find the direction of relation
                if(memberA.equals(r.getMemberA())){
                    member = r.getMemberB();
                    cis = true;
                }
                else{
                    member = r.getMemberA();
                    cis = false;
                }
                // Make sure "member" is new to the list "marrow"
                boolean exist = false;
                for(int j=0;j<marrow.size();j++){
                    if(member.equals(marrow.get(j).Other)) {
                        exist = true;
                        break;
                    }
                }
                if(exist) continue;
                // Take one step further
                int tail = marrow.size();
                MARROW newMarrow = new MARROW();
                newMarrow.Other = member;
                newMarrow.Relation = r.getRelation();
                newMarrow.CIS = cis;
                marrow.add(newMarrow);
                marrow = MarrowPuncture(Manager, member, memberB, marrow);
                if(marrow.size() > tail)
                    return marrow;
                marrow.remove(tail - 1);
            }
            return marrow;
        } catch (DataBaseError e) {
            throw e;
        } catch (Exception e) {
            Log.i("TAG", "MarrowPuncture : " + e.getMessage() + e.getLocalizedMessage());
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_MarrowPuncture);
        }
    }

    // Count the distance between two people, return -1 means unlinked
    // Users can identify weight for each type of relation
    int Stadiometry(Realm Manager, String memberA, String memberB,
                    int indexParent, int indexSpouse, int indexSibling,
                    int indexDivorce, int indexUnknown, int indexFriend)
            throws DataBaseError {
        try {
            PrimaryMemberIDCheck(Manager, memberA);
            PrimaryMemberIDCheck(Manager, memberB);

            // Estimate the size of marrow list
            List<MemberData> memberListA = getMemberList(Manager, memberA, "", "", "");
            List<MemberData> memberListB = getMemberList(Manager, memberB, "", "", "");
            String familyIDA = (null == memberListA) ? "" : (memberListA.size() > 0) ? memberListA.get(0).getFamilyID() : "";
            String familyIDB = (null == memberListB) ? "" : (memberListB.size() > 0) ? memberListB.get(0).getFamilyID() : "";
            int indexEstimateA = familyIDA.equals("") ? 1 : getMemberList(Manager, "", familyIDA, "", "").size();
            int indexEstimateB = familyIDB.equals(familyIDA) ? 1 :
                    familyIDB.equals("") ? 1 : getMemberList(Manager, "", familyIDB, "", "").size();

            // Calculate the distance
            List<MARROW> marrow = new ArrayList<>(indexEstimateA + indexEstimateB);
            marrow = MarrowPuncture(Manager, memberA, memberB, marrow);
            if(null == marrow || marrow.size() <= 0) return -1;
            MARROW m;
            int dist = 0;
            for(int i=0;i<marrow.size();i++){
                m = marrow.get(i);
                if(MemberRelationData.DB_RELATION_AMOUNT <= m.Relation) {
                    Log.i("TAG", "Stadiometry : UnspecifiedRelation");
                    throw new DataBaseError(DataBaseError.ErrorType.UnspecifiedRelation);
                }
                else if(MemberRelationData.DB_RELATION_PARENT == m.Relation) dist += indexParent;
                else if(MemberRelationData.DB_RELATION_SPOUSE == m.Relation) dist += indexSpouse;
                else if(MemberRelationData.DB_RELATION_SIBLING == m.Relation) dist += indexSibling;
                else if(MemberRelationData.DB_RELATION_DIVORCE == m.Relation) dist += indexDivorce;
                else if(MemberRelationData.DB_RELATION_UNKNOWN == m.Relation) dist += indexUnknown;
                else if(MemberRelationData.DB_RELATION_FRIEND == m.Relation) dist += indexFriend;
                else if(MemberRelationData.DB_RELATION_ONESELF == m.Relation) dist += 0;
                else {
                    Log.i("TAG", "Stadiometry : UnspecifiedRelation");
                    throw new DataBaseError(DataBaseError.ErrorType.UnspecifiedRelation);
                }
            }
            return dist;
        } catch (DataBaseError e) {
            throw e;
        } catch (Exception e) {
            Log.i("TAG", "Stadiometry : " + e.getMessage() + e.getLocalizedMessage());
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_Stadiometry);
        }
    }

    // Translate relation between two members into words
    String Transcription(Realm Manager, String memberA, String memberB) throws DataBaseError {
        try {
            PrimaryMemberIDCheck(Manager, memberA);
            PrimaryMemberIDCheck(Manager, memberB);
            String appellation = "";



            return appellation;
        } catch (DataBaseError e) {
            throw e;
        } catch (Exception e) {
            Log.i("TAG", "Transcription : " + e.getMessage() + e.getLocalizedMessage());
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_Transcription);
        }
    }

    // Attention : Remember to import .DataBaseManager.RelationRadiationChart && .DataBaseManager.RelationStrengthIndex
    // Draw the local focused family member relation tree
    RelationRadiationChart GetLocalFocusedMap(Realm Manager, String member) throws DataBaseError {
        try {
            PrimaryMemberIDCheck(Manager, member);
            RelationRadiationChart chart = new RelationRadiationChart();
            List<MemberRelationData> ParentList = getMemberRelationList(Manager, member, MemberRelationData.DB_RELATION_PARENT);
            List<MemberRelationData> SiblingList = getMemberRelationList(Manager, member, MemberRelationData.DB_RELATION_SIBLING);
            List<MemberRelationData> OneselfList = getMemberRelationList(Manager, member, MemberRelationData.DB_RELATION_ONESELF);
            List<MemberRelationData> PredecessorList = getMemberRelationList(Manager, member, MemberRelationData.DB_RELATION_DIVORCE);
            chart.spouse = new RelationStrengthIndex();
            chart.parentList = new ArrayList<>(ParentList.size()+1);
            chart.siblingList = new ArrayList<>(SiblingList.size()+1);
            chart.childList = new ArrayList<>(ParentList.size()+1);
            chart.oneselfList = new ArrayList<>(OneselfList.size()+1);
            chart.predecessorList = new ArrayList<>(PredecessorList.size()+1);

            return chart;
        } catch (DataBaseError e) {
            throw e;
        } catch (Exception e) {
            Log.i("TAG", "getLocalFocusedMap : " + e.getMessage() + e.getLocalizedMessage());
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_GetLocalFocusedMap);
        }

    }

    // Only lazy deletion is safe, or ID conflict will occur
    FamilyData AddFamily(Realm Manager, String name, String memberID)
            throws DataBaseError {
        try {
            RealmResults<MemberData> Result = Manager.where(MemberData.class)
                    .equalTo("ID",memberID).findAll();
            if(!memberID.equals("")){
                if(Result.size() <= 0) {
                    Log.i("TAG", "AddFamily : MemberNotExist");
                    throw new DataBaseError(DataBaseError.ErrorType.MemberNotExist);
                }
                else if(Result.size() > 1) {
                    Log.i("TAG", "AddFamily : FamilyRedundant");
                    throw new DataBaseError(DataBaseError.ErrorType.UnknownError_MemberData);
                }
                else if(!Result.first().getFamilyID().equals("")) {
                    Log.i("TAG", "AddFamily : MemberHasFamilyAlready");
                    throw new DataBaseError(DataBaseError.ErrorType.MemberHasFamilyAlready);
                }
                else if(!Result.first().getActivate()) {
                    Log.i("TAG", "AddFamily : MemberHibernating");
                    throw new DataBaseError(DataBaseError.ErrorType.MemberHibernating);
                }
            }

            Manager.beginTransaction();
            FamilyData newFamily = Manager.createObject(FamilyData.class,
                    String.valueOf(Manager.where(FamilyData.class).findAll().size()));
            newFamily.setName(name);
            newFamily.setRootMemberID(memberID);
            newFamily.setActivate(true);
            Manager.commitTransaction();

            // Family ID update for member must be after completion of family
            Manager.beginTransaction();
            if(!memberID.equals(""))
                Result.first().setFamilyID(newFamily.getID());
            Manager.commitTransaction();
            return newFamily;
        } catch (DataBaseError e) {
            throw e;
        } catch (Exception e) {
            Log.i("TAG", "AddFamily : " + e.getMessage() + e.getLocalizedMessage());
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_AddFamily);
        }
    }

    MemberData AddMember(Realm Manager, String nickname, String password, String familyID,
                         String name, int gender, String phone)
            throws DataBaseError {
        try {
            RealmResults<FamilyData> Result = Manager.where(FamilyData.class)
                    .equalTo("ID",familyID).findAll();
            if(!familyID.equals("")){
                if(Result.size() <= 0) {
                    Log.i("TAG", "AddMember : FamilyNotExist");
                    throw new DataBaseError(DataBaseError.ErrorType.FamilyNotExist);
                }
                else if(Result.size() > 1) {
                    Log.i("TAG", "AddMember : FamilyRedundant");
                    throw new DataBaseError(DataBaseError.ErrorType.UnknownError_FamilyData);
                }
                else if(!Result.first().getActivate()) {
                    Log.i("TAG", "AddMember : FamilyHibernating");
                    throw new DataBaseError(DataBaseError.ErrorType.FamilyHibernating);
                }
            }

            // add member info
            Manager.beginTransaction();
            MemberData newMember = Manager.createObject(MemberData.class,
                    String.valueOf(Manager.where(MemberData.class).findAll().size()));
            newMember.setFamilyID(familyID);
            newMember.setName(name);
            newMember.setGender(gender);
            newMember.setNickName(nickname);
            newMember.setPassword(password);
            newMember.setActivate(true);
            Manager.commitTransaction();

            // Root Member ID update for family must be after completion of member
//            Manager.beginTransaction();
//            if(Result.first().getRootMemberID().equals(""))
//                Result.first().setRootMemberID(newMember.getID());
//            Manager.commitTransaction();

            // add phone info
            try {
                AddPhone(Manager, phone, newMember.getID());
            } catch (DataBaseSignal s) {
                if(DataBaseSignal.SignalType.PhoneAddedAlready != s.getSignalType()) {
                    Log.i("TAG", "AddPhone : " + s.getSignalType() + s.getMessage() + s.getLocalizedMessage());
                    throw s;
                }
            } catch (DataBaseError e) {
                throw e;
            } catch (Exception e) {
                Log.i("TAG", "AddPhone : " + e.getMessage() + e.getLocalizedMessage());
                throw new DataBaseError(DataBaseError.ErrorType.UnknownError_AddPhone);
            }

            return newMember;
        } catch (DataBaseError e) {
            throw e;
        } catch (Exception e) {
            Log.i("TAG", "AddMember : " + e.getMessage() + e.getLocalizedMessage());
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_AddMember);
        }
    }

    void AddExistMemberToExistFamily(Realm Manager, String familyID, String memberID)
            throws DataBaseError, DataBaseSignal {
        try {
            PrimaryMemberIDCheck(Manager, memberID);
            PrimaryFamilyIDCheck(Manager, familyID);

            // change direct member data
            Manager.beginTransaction();
            MemberData updateMember = Manager.createObject(MemberData.class, memberID);
            String oldFamilyID = updateMember.getFamilyID();
            updateMember.setFamilyID(familyID);
            FamilyData ResultF = Manager.createObject(FamilyData.class, familyID);
            if(ResultF.getRootMemberID().equals(""))
                ResultF.setRootMemberID(memberID);
            Manager.commitTransaction();

            // change original family data
            Manager.beginTransaction();
            FamilyData updateFamily = Manager.createObject(FamilyData.class, oldFamilyID);
            updateFamily.setActivate(false);
            Manager.commitTransaction();

            // check and update other original family member
            boolean single;
            Manager.beginTransaction();
            RealmResults<MemberData> ResultM = Manager.where(MemberData.class).equalTo("familyID", oldFamilyID).findAll();
            if(ResultM.size() > 0){
                int telomere = ResultM.size();
                do{
                    updateMember = ResultM.first();
                    updateMember.setFamilyID(familyID);
                } while(ResultM.size() > 0 && --telomere > 0);
                if(ResultM.size() > 0) {
                    Log.i("TAG", "AddExistMemberToExistFamily : RealmResultAutoUpdateFail");
                    throw new DataBaseError(DataBaseError.ErrorType.RealmResultAutoUpdateFail);
                }
                single = false;
            }
            else single = true;
            Manager.commitTransaction();
            if(single) throw new DataBaseSignal(DataBaseSignal.SignalType.AddSingleMemberToFamilySucceed);
            else throw new DataBaseSignal(DataBaseSignal.SignalType.MergeTwoFamiliesSucceed);
        } catch (DataBaseError | DataBaseSignal e) {
            throw e;
        } catch(Exception e){
            Log.i("TAG", "AddExistMemberToExistFamily : " + e.getMessage() + e.getLocalizedMessage());
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_MemberData);
        }
    }

    void AddPhone(Realm Manager, String phone, String memberID)
            throws DataBaseSignal, DataBaseError {
        try {
            PrimaryMemberIDCheck(Manager, memberID);
            RealmResults<PhoneData> Result = Manager.where(PhoneData.class).equalTo("phone", phone).findAll();
            if (Result.size() > 0) {
                Log.i("TAG", "AddPhone : PhoneNumberConflict");
                throw new DataBaseError(DataBaseError.ErrorType.PhoneNumberConflict);
            }

            Manager.beginTransaction();
            PhoneData newPhone = Manager.createObject(PhoneData.class, phone);
            newPhone.setMemberID(memberID);
            Manager.commitTransaction();
            throw new DataBaseSignal(DataBaseSignal.SignalType.PhoneAddedAlready);
        } catch (DataBaseError | DataBaseSignal e) {
            throw e;
        } catch (Exception e) {
            Log.i("TAG", "AddPhone : " + e.getMessage() + e.getLocalizedMessage());
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_AddPhone);
        }
    }

    void AddGameRecord(Realm Manager, String memberID, double factor, long date, int gameType)
            throws DataBaseSignal, DataBaseError {
        try {
            PrimaryMemberIDCheck(Manager, memberID);
            Manager.beginTransaction();
            GameRecordData newRecord = Manager.createObject(GameRecordData.class,
                    String.valueOf(Manager.where(GameRecordData.class).findAll().size()));
            newRecord.setFactor(factor);
            newRecord.setGameType(gameType);
            newRecord.setMemberID(memberID);
            newRecord.setDate(date);
            newRecord.setActivate(true);
            Manager.commitTransaction();
            throw new DataBaseSignal(DataBaseSignal.SignalType.GameRecordAddedAlready);
        } catch (DataBaseError | DataBaseSignal e) {
            throw e;
        } catch (Exception e) {
            Log.i("TAG", "AddGameRecord : " + e.getMessage() + e.getLocalizedMessage());
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_AddGameRecord);
        }
    }

    // Attention : A is parent to B / A is hasband and B is wife
    void AddMemberRelation(Realm Manager, String memberA, String memberB, int relation)
            throws DataBaseSignal, DataBaseError {
        try {
            PrimaryMemberIDCheck(Manager, memberA);
            PrimaryMemberIDCheck(Manager, memberB);
            if(Stadiometry(Manager, memberA, memberB, 1, 1, 1, 1, 1, 1) >= 0) {
                Log.i("TAG", "AddMemberRelation : RelationError_TryingToCombineTwoLinkedTree");
                throw new DataBaseError(DataBaseError.ErrorType.RelationError_TryingToCombineTwoLinkedTree);
            }

            // Reach sibling list terminal
            if(MemberRelationData.DB_RELATION_SIBLING == relation) {
                memberA = FindSiblingListTail(Manager, memberA);
                memberB = FindSiblingListHead(Manager, memberB);
            }
            else if(MemberRelationData.DB_RELATION_PARENT == relation) {
                memberB = FindSiblingListHead(Manager, memberB);
            }

            // Relation Check
            List<MemberRelationData> listA = getMemberRelationList(Manager, memberA, 0);
            List<MemberRelationData> listB = getMemberRelationList(Manager, memberB, 0);
            MemberRelationData m;

            // number of A/B's parent / sibling / child / spouse
            int parentA = 0;
            int siblingA = 0;
            int childA = 0;
            int maritalA = 0;
            int parentB = 0;
            int siblingB = 0;
            int maritalB = 0;
            int childB = 0;
            // store the ID of some roles for convenience
            String ChildIDA = "";
            String ParentIDA = "";
            int telomere_A = listA.size();
            int telomere_B = listB.size();

            // First deal with A-related relations
            for(int i=0;i<listA.size();i++){
                if(listA.size() != telomere_A) {
                    Log.i("TAG", "AddMemberRelation : RealmListExceptionallyUpdated");
                    throw new DataBaseError(DataBaseError.ErrorType.RealmListExceptionallyUpdated);
                }
                m = listA.get(i);
                String member = memberA.equals(m.getMemberA()) ? m.getMemberB() : m.getMemberA();

                // A and B has relation already
                if(memberB.equals(member)) {
                    Log.i("TAG", "AddMemberRelation : RelationError_Redundant");
                    throw new DataBaseError(DataBaseError.ErrorType.RelationError_Redundant);
                }

                // Standardize oneself relation
                else if(MemberRelationData.DB_RELATION_ONESELF == m.getRelation()
                        && getMemberRelationList(Manager, member,0).size() > 1){
                    try {
                        AutoTransplanting(Manager, memberA, member);
                    } catch (DataBaseSignal s) {
                        if(DataBaseSignal.SignalType.AutoTransplantationSucceed != s.getSignalType()) {
                            Log.i("TAG", "AddMemberRelation : " + s.getSignalType() + s.getMessage() + s.getLocalizedMessage());
                            throw s;
                        }
                    }
                    // Recursion : Refresh the lists and re-do adding operation
                    // It will break by throwing a signal
                    AddMemberRelation(Manager, memberA, memberB, relation);
                }

                // When A has a parent / child
                else if(MemberRelationData.DB_RELATION_PARENT == m.getRelation()){
                    ParentIDA = member;
                    // When A has a child
                    if(memberA.equals(m.getMemberA())){
                        ChildIDA = member;
                        ParentIDA = "";
                        // Original error : 2-child error
                        if(++childA > 1){
                            Log.i("TAG", "AddMemberRelation : RelationError_Blood_TwoChildStructure");
                            throw new DataBaseError(DataBaseError.ErrorType.RelationError_Blood_TwoChildStructure);
                        }
                        // Transfer from double child to child-sibling structure
                        // Note : Change relation-adding method here, it will break by throwing a signal
                        else if(MemberRelationData.DB_RELATION_PARENT == relation){
                            AddMemberRelation(
                                    Manager,
                                    FindSiblingListTail(Manager, member),
                                    FindSiblingListHead(Manager, memberB),
                                    MemberRelationData.DB_RELATION_SIBLING);
                        }
                    }
                    // When A has a parent
                    // Original error : 2-parent error
                    else if(++parentA > 1) {
                        Log.i("TAG", "AddMemberRelation : RelationError_Blood_TwoParentStructure");
                        throw new DataBaseError(DataBaseError.ErrorType.RelationError_Blood_TwoParentStructure);
                    }
                    // Original error : mixed 3-blood error
                    else if (parentA + siblingA > 2) {
                        Log. i("TAG", "AddMemberRelation : RelationError_Blood_MixedThreeBloodStructure");
                        throw new DataBaseError(DataBaseError.ErrorType.RelationError_Blood_MixedThreeBloodStructure);
                    }
                    // Transferring due to mixed 2-blood structure
                    // Note : Change relation-adding method here, it will break by throwing a signal
                    else if(siblingA > 0 && MemberRelationData.DB_RELATION_SIBLING == relation){
                        while(MemberRelationData.DB_RELATION_SIBLING != m.getRelation())
                            m = listA.get(--i);
                        member = memberA.equals(m.getMemberA()) ? m.getMemberB() : m.getMemberA();
                        AddMemberRelation(
                                Manager,
                                FindSiblingListTail(Manager, member),
                                FindSiblingListHead(Manager, memberB),
                                MemberRelationData.DB_RELATION_SIBLING);
                    }
                }
                // When A has a spouse
                else if(MemberRelationData.DB_RELATION_SPOUSE == m.getRelation()){
                    // Error : Conflict spouses
                    if(++maritalA > 1 || MemberRelationData.DB_RELATION_SPOUSE == relation) {
                        Log.i("TAG", "AddMemberRelation : RelationError_Bigamy");
                        throw new DataBaseError(DataBaseError.ErrorType.RelationError_Bigamy);
                    }
                }
                // When A has a sibling
                else if(MemberRelationData.DB_RELATION_SIBLING == m.getRelation()) {
                    // Original error : 3-sibling error
                    if (++siblingA > 2) {
                        Log.i("TAG", "AddMemberRelation : RelationError_Blood_ThreeSiblingStructure");
                        throw new DataBaseError(DataBaseError.ErrorType.RelationError_Blood_ThreeSiblingStructure);
                    }
                    // Original error : mixed 3-blood error
                    else if (parentA + siblingA > 2) {
                        Log.i("TAG", "AddMemberRelation : RelationError_Blood_MixedThreeBloodStructure");
                        throw new DataBaseError(DataBaseError.ErrorType.RelationError_Blood_MixedThreeBloodStructure);
                    }
                    // Transferring due to mixed 2-blood structure
                    // Note : Change relation-adding method here, it will break by throwing a signal
                    else if (parentA + siblingA > 1 && MemberRelationData.DB_RELATION_SIBLING == relation) {
                        AddMemberRelation(
                                Manager,
                                FindSiblingListTail(Manager, member),
                                FindSiblingListHead(Manager, memberB),
                                MemberRelationData.DB_RELATION_SIBLING);
                    }
                }
            }

            // Then deal with B-related relations
            for(int i=0;i<listB.size();i++){
                if(listB.size() != telomere_B) {
                    Log.i("TAG", "AddMemberRelation : RealmListExceptionallyUpdated");
                    throw new DataBaseError(DataBaseError.ErrorType.RealmListExceptionallyUpdated);
                }
                m = listB.get(i);
                String member = memberB.equals(m.getMemberA()) ? m.getMemberB() : m.getMemberA();

                // A and B has relation already
                if(memberA.equals(member)) {
                    Log.i("TAG", "AddMemberRelation : RelationError_Redundant");
                    throw new DataBaseError(DataBaseError.ErrorType.RelationError_Redundant);
                }

                // Standardize oneself relation
                else if(MemberRelationData.DB_RELATION_ONESELF == m.getRelation()
                        && getMemberRelationList(Manager, member,0).size() > 1){
                    try {
                        AutoTransplanting(Manager, memberB, member);
                    } catch (DataBaseSignal s) {
                        if(DataBaseSignal.SignalType.AutoTransplantationSucceed != s.getSignalType()) {
                            Log.i("TAG", "AddMemberRelation : " + s.getSignalType() + s.getMessage() + s.getLocalizedMessage());
                            throw s;
                        }
                    }
                    // Recursion : Refresh the lists and re-do adding operation
                    // It will break by throwing a signal
                    AddMemberRelation(Manager, memberA, memberB, relation);
                }

                // When B has a parent / child
                else if(MemberRelationData.DB_RELATION_PARENT == m.getRelation()){
                    // When B has a child
                    if(memberB.equals(m.getMemberA())) {
                        // Original error : 2-child error
                        if (++childB > 1) {
                            Log.i("TAG", "AddMemberRelation : RelationError_Blood_TwoChildStructure");
                            throw new DataBaseError(DataBaseError.ErrorType.RelationError_Blood_TwoChildStructure);
                        }
                        // LABEL : Spouse_SeparateParentFromSiblingStructure
                        // Pre-address in case of separate-parent-from-sibling structure
                        else if(childA > 0 && MemberRelationData.DB_RELATION_SPOUSE == relation){
                            try{
                                DestroyRelation(Manager, member, memberB);
                            } catch (DataBaseSignal s) {
                                if(DataBaseSignal.SignalType.MemberRelationDeleted != s.getSignalType()){
                                    Log.i("TAG", "AddMemberRelation : " + s.getSignalType() + s.getMessage() + s.getLocalizedMessage());
                                    throw s;
                                }
                            }
                            try{
                                AddMemberRelation(
                                        Manager,
                                        FindSiblingListTail(Manager, ChildIDA),
                                        FindSiblingListHead(Manager, member),
                                        MemberRelationData.DB_RELATION_SIBLING);
                            } catch (DataBaseSignal s) {
                                if(DataBaseSignal.SignalType.MemberRelationAddedAlready != s.getSignalType()){
                                    Log.i("TAG", "AddMemberRelation : " + s.getSignalType() + s.getMessage() + s.getLocalizedMessage());
                                    throw s;
                                }
                            }
                            childB--;
                        }
                    }
                    // When B has a parent
                    // Original error : 2-parent error
                    else if(++parentB > 1){
                        Log.i("TAG", "AddMemberRelation : RelationError_Blood_TwoParentStructure");
                        throw new DataBaseError(DataBaseError.ErrorType.RelationError_Blood_TwoParentStructure);
                    }
                    // Original error : mixed 3-blood error
                    if(parentB + siblingB > 2) {
                        Log.i("TAG", "AddMemberRelation : RelationError_Blood_MixedThreeBloodStructure");
                        throw new DataBaseError(DataBaseError.ErrorType.RelationError_Blood_MixedThreeBloodStructure);
                    }
                    // Transfer from double parent to parent-spouse structure
                    // Note : Change relation-adding method here, it will break by throwing a signal
                    else if(MemberRelationData.DB_RELATION_PARENT == relation){
                        AddMemberRelation(Manager, member, memberA, MemberRelationData.DB_RELATION_SPOUSE);
                    }
                    // LABEL : Sibling_SeparateParentFromSiblingStructure
                    // When A and B are siblings, B's parent must be linked to A so that only one parent will link
                    else if(MemberRelationData.DB_RELATION_SIBLING == relation){
                        // Cut B and its parent
                        try {
                            DestroyRelation(Manager, member, memberB);
                        } catch (DataBaseSignal s) {
                            if(DataBaseSignal.SignalType.MemberRelationDeleted != s.getSignalType()) {
                                Log.i("TAG", "AddMemberRelation : " + s.getSignalType() + s.getMessage() + s.getLocalizedMessage());
                                throw s;
                            }
                        }
                        // A married B's parent
                        if(parentA > 0) {
                            try {
                                AddMemberRelation(Manager, ParentIDA, member, MemberRelationData.DB_RELATION_SPOUSE);
                            } catch (DataBaseSignal s) {
                                if(DataBaseSignal.SignalType.MemberRelationAddedAlready != s.getSignalType()) {
                                    Log.i("TAG", "AddMemberRelation : " + s.getSignalType() + s.getMessage() + s.getLocalizedMessage());
                                    throw s;
                                }
                            }
                        }
                        // A got a parent
                        else {
                            try {
                                AddMemberRelation(Manager, member, memberA, MemberRelationData.DB_RELATION_PARENT);
                            } catch (DataBaseSignal s) {
                                if (DataBaseSignal.SignalType.MemberRelationAddedAlready != s.getSignalType()) {
                                    Log.i("TAG", "AddMemberRelation : " + s.getSignalType() + s.getMessage() + s.getLocalizedMessage());
                                    throw s;
                                }
                            }
                            parentA++;
                        }
                        parentB--;
                    }
                }
                // When B has a spouse
                else if(MemberRelationData.DB_RELATION_SPOUSE == m.getRelation()){
                    // Error : Conflict spouses
                    if(++maritalB > 1 || MemberRelationData.DB_RELATION_SPOUSE == relation) {
                        Log.i("TAG", "AddMemberRelation : RelationError_Bigamy");
                        throw new DataBaseError(DataBaseError.ErrorType.RelationError_Bigamy);
                    }
                }
                // When B has a sibling
                else if(MemberRelationData.DB_RELATION_SIBLING == m.getRelation()) {
                    // Original error : 3-sibling error
                    if (++siblingB > 2) {
                        Log.i("TAG", "AddMemberRelation : RelationError_Blood_ThreeSiblingStructure");
                        throw new DataBaseError(DataBaseError.ErrorType.RelationError_Blood_ThreeSiblingStructure);
                    }
                    // Original error : mixed 3-blood error
                    else if (parentB + siblingB > 2 || parentB + siblingB > 1 && MemberRelationData.DB_RELATION_SIBLING == relation) {
                        Log.i("TAG", "AddMemberRelation : RelationError_Blood_MixedThreeBloodStructure");
                        throw new DataBaseError(DataBaseError.ErrorType.RelationError_Blood_MixedThreeBloodStructure);
                    }
                }
            }

            // Final congenital drawbacks check
            // Error : Conflict spouses
            if (maritalA > 1 || maritalB > 1 || maritalA + maritalB > 0 && MemberRelationData.DB_RELATION_SPOUSE == relation) {
                Log.i("TAG", "AddMemberRelation : RelationError_Bigamy");
                throw new DataBaseError(DataBaseError.ErrorType.RelationError_Bigamy);
            }
            // Original error : 3-sibling error
            if (siblingA > 2 || siblingB > 2 || (siblingA > 1 || siblingB > 1) && MemberRelationData.DB_RELATION_SIBLING == relation) {
                Log.i("TAG", "AddMemberRelation : RelationError_Blood_ThreeSiblingStructure");
                throw new DataBaseError(DataBaseError.ErrorType.RelationError_Blood_ThreeSiblingStructure);
            }
            // Original error : 2-parent error
            if (parentA > 1 || parentB > 1 || parentB > 0 && MemberRelationData.DB_RELATION_PARENT == relation) {
                Log.i("TAG", "AddMemberRelation : RelationError_Blood_TwoParentStructure");
                throw new DataBaseError(DataBaseError.ErrorType.RelationError_Blood_TwoParentStructure);
            }
            // Original error : 2-child error
            if (childA > 1 || childB > 1 || childA > 0 && MemberRelationData.DB_RELATION_PARENT == relation) {
                Log.i("TAG", "AddMemberRelation : RelationError_Blood_TwoChildStructure");
                throw new DataBaseError(DataBaseError.ErrorType.RelationError_Blood_TwoChildStructure);
            }
            // Original error : mixed 3-blood error
            if (parentA + siblingA > 2 || parentB + siblingB > 2) {
                Log.i("TAG", "AddMemberRelation : RelationError_Blood_MixedThreeBloodStructure");
                throw new DataBaseError(DataBaseError.ErrorType.RelationError_Blood_MixedThreeBloodStructure);
            }

            // Spouse check
            if (MemberRelationData.DB_RELATION_SPOUSE == relation) {
                List<MemberData> memA = getMemberList(Manager, memberA, "", "", "");
                List<MemberData> memB = getMemberList(Manager, memberB, "", "", "");
                int genderA = memA.get(0).getGender();
                int genderB = memB.get(0).getGender();
                if (genderA == genderB && (genderA == MemberData.DB_GENDER_MALE || genderB == MemberData.DB_GENDER_FEMALE)) {
                    Log.i("TAG", "AddMemberRelation : RelationError_HomoMarriage");
                    throw new DataBaseError(DataBaseError.ErrorType.RelationError_HomoMarriage);
                }
                else if(MemberData.DB_GENDER_FEMALE == genderA) {
                    String str;
                    str = memberA;
                    memberA = memberB;
                    memberB = str;
                }
                // It should be addressed at LABEL : "Spouse_SeparateParentFromSiblingStructure"
                if(childA > 0 && childB > 0) {
                    Log.i("TAG", "AddMemberRelation : RelationError_Blood_SeparateParentFromSiblingStructure");
                    throw new DataBaseError(DataBaseError.ErrorType.RelationError_Blood_SeparateParentFromSiblingStructure);
                }
            }
            // Parent check
            if (MemberRelationData.DB_RELATION_PARENT == relation) {
                if(parentA + siblingA > 1 || parentB + siblingB > 1) {
                    Log.i("TAG", "AddMemberRelation : RelationError_Blood_MixedThreeBloodStructure");
                    throw new DataBaseError(DataBaseError.ErrorType.RelationError_Blood_MixedThreeBloodStructure);
                }
            }
            // Sibling check
            // Note : SeparateParentFromSiblingStructure Problem will be addressed after sibling list is built
            if (MemberRelationData.DB_RELATION_SIBLING == relation) {
                if(parentA + siblingA > 1 || parentB + siblingB > 1) {
                    Log.i("TAG", "AddMemberRelation : RelationError_Blood_MixedThreeBloodStructure");
                    throw new DataBaseError(DataBaseError.ErrorType.RelationError_Blood_MixedThreeBloodStructure);
                }
                // It should be addressed at LABEL : "Sibling_SeparateParentFromSiblingStructure"
                if(parentA > 0 && parentB > 0) {
                    Log.i("TAG", "AddMemberRelation : RelationError_Blood_SeparateParentFromSiblingStructure");
                    throw new DataBaseError(DataBaseError.ErrorType.RelationError_Blood_SeparateParentFromSiblingStructure);
                }
            }

            // Build relation
            Manager.beginTransaction();
            MemberRelationData newRelation = Manager.createObject(MemberRelationData.class);
            newRelation.setMemberA(memberA);
            newRelation.setMemberB(memberB);
            newRelation.setRelation(relation);
            Manager.commitTransaction();
            throw new DataBaseSignal(DataBaseSignal.SignalType.MemberRelationAddedAlready);
        } catch (DataBaseError | DataBaseSignal e) {
            throw e;
        } catch (Exception e) {
            Log.i("TAG", "AddMemberRelation : " + e.getMessage() + e.getLocalizedMessage());
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_AddRelation);
        }
    }

    void AddImage(Realm Manager, String memberID, String name, String location, String imagePath,
                  long date, String note, String parentImage)
            throws DataBaseSignal, DataBaseError {
        try {
            PrimaryMemberIDCheck(Manager, memberID);
            if (!parentImage.equals("")) {
                try {
                    PrimaryPictureIDCheck(Manager, parentImage);
                } catch (DataBaseError e) {
                    if(DataBaseError.ErrorType.ImageNotExist == e.getErrorType()) {
                        Log.i("TAG", "AddImage : ParentImageNotExist");
                        throw new DataBaseError(DataBaseError.ErrorType.ParentImageNotExist);
                    }
                    else {
                        Log.i("TAG", "AddImage : " + e.getMessage() + e.getLocalizedMessage());
                        throw e;
                    }
                }
            }
            Manager.beginTransaction();
            PictureData newImage = Manager.createObject(PictureData.class,
                    String.valueOf(Manager.where(PictureData.class).findAll().size()));
            if(!imagePath.equals(""))
                newImage.setImagePath(imagePath);
            else
                newImage.setImagePath(".ImageStore." + newImage.getID());
            newImage.setMemberID(memberID);
            newImage.setDate(date);
            newImage.setLocation(location);
            newImage.setName(name);
            newImage.setNote(note);
            newImage.setParentImage(parentImage);
            newImage.setActivate(true);
            Manager.commitTransaction();
            throw new DataBaseSignal(DataBaseSignal.SignalType.ImageAddedAlready);
        } catch (DataBaseError | DataBaseSignal e) {
            throw e;
        } catch (Exception e) {
            Log.i("TAG", "AddImage : " + e.getMessage() + e.getLocalizedMessage());
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_AddImage);
        }
    }

    void AddThirdPartyAccount(Realm Manager, String memberID, String account, int thirdPartyType)
            throws DataBaseSignal, DataBaseError {
        try {
            PrimaryMemberIDCheck(Manager, memberID);
            RealmResults<ThirdPartyAccountData> Result = Manager.where(ThirdPartyAccountData.class)
                    .equalTo("account", account).equalTo("thirdPartyType", thirdPartyType).findAll();
            if (Result.size() > 0) {
                Log.i("TAG", "AddThirdPartyAccount : ThirdPartyAccountConflict");
                throw new DataBaseError(DataBaseError.ErrorType.ThirdPartyAccountConflict);
            }
            Manager.beginTransaction();
            ThirdPartyAccountData newAccount = Manager.createObject(ThirdPartyAccountData.class);
            newAccount.setMemberID(memberID);
            newAccount.setAccount(account);
            newAccount.setThirdPartyType(thirdPartyType);
            Manager.commitTransaction();
            throw new DataBaseSignal(DataBaseSignal.SignalType.ThirdPartyAccountAddedAlready);
        } catch (DataBaseError | DataBaseSignal e) {
            throw e;
        } catch (Exception e) {
            Log.i("TAG", "AddThirdPartyAccount : " + e.getMessage() + e.getLocalizedMessage());
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_AddThirdPartyAccount);
        }
    }

    List<PhoneData> getPhoneList(Realm Manager, String phone, String memberID) throws DataBaseError {
        RealmQuery<PhoneData> Results = Manager.where(PhoneData.class);
        if (!phone.equals(""))
            Results = Results.equalTo("phone", phone);
        if (!memberID.equals(""))
            Results = Results.equalTo("memberID", memberID);
        List<PhoneData> l =Manager.copyFromRealm(Results.findAll());
        return (null == l) ? new ArrayList<PhoneData>(1) : l;
    }

    List<FamilyData> getFamilyList(Realm Manager, String ID, String name, String rootMemberID) throws DataBaseError {
        RealmQuery<FamilyData> Results = Manager.where(FamilyData.class);
        if (!ID.equals(""))
            Results = Results.equalTo("ID", ID);
        if (!name.equals(""))
            Results = Results.equalTo("name", name);
        if (!rootMemberID.equals(""))
            Results = Results.equalTo("rootMemberID", rootMemberID);
        List<FamilyData> l = Manager.copyFromRealm(Results.equalTo("activate",true).findAll());
        return (null == l) ? new ArrayList<FamilyData>(1) : l;
    }

    List<MemberData> getMemberList(Realm Manager, String ID, String familyID, String name, String nickname) throws DataBaseError {
        RealmQuery<MemberData> Results = Manager.where(MemberData.class);
        if (!ID.equals(""))
            Results = Results.equalTo("ID", ID);
        if (!familyID.equals(""))
            Results = Results.equalTo("familyID", familyID);
        if (!name.equals(""))
            Results = Results.equalTo("name", name);
        if (!nickname.equals(""))
            Results = Results.equalTo("nickname", nickname);
        List<MemberData> l = Manager.copyFromRealm(Results.equalTo("activate",true).findAll());
        return (null == l) ? new ArrayList<MemberData>(1) : l;
    }

    // Note : Member could be inactive; relation "0" or negative number means no restriction
    List<MemberRelationData> getMemberRelationList(Realm Manager, String member, int relation) throws DataBaseError {
        RealmQuery<MemberRelationData> Results = Manager.where(MemberRelationData.class);
        if (!member.equals(""))
            Results = Results.equalTo("memberA", member).or().equalTo("memberB", member);
        if(0 < relation)
            Results = Results.equalTo("relation",relation);
        List<MemberRelationData> l = Manager.copyFromRealm(Results.findAll());
        return (null == l) ? new ArrayList<MemberRelationData>(1) : l;
    }

    List<GameRecordData> getGameRecordList(Realm Manager, String recordID, String memberID, int gameType,
                                           long dateBegin, long dateEnd) throws DataBaseError {
        RealmQuery<GameRecordData> Results = Manager.where(GameRecordData.class);
        if (!recordID.equals(""))
            Results = Results.equalTo("recordID", recordID);
        if (!memberID.equals(""))
            Results = Results.equalTo("memberID", memberID);
        if (0 != gameType)
            Results = Results.equalTo("gameType", gameType);
        if(0 != dateBegin)
            Results = Results.greaterThanOrEqualTo("date",dateBegin);
        if(0 != dateEnd)
            Results = Results.lessThanOrEqualTo("date",dateEnd);
        List<GameRecordData> l = Manager.copyFromRealm(Results.equalTo("activate",true).findAll());
        return (null == l) ? new ArrayList<GameRecordData>(1) : l;
    }

    List<PictureData> getPictureList(Realm Manager, String ID, String name, String memberID,
                                     String location, String parentImage,
                                     long dateBegin, long dateEnd) throws DataBaseError {
        RealmQuery<PictureData> Results = Manager.where(PictureData.class);
        if (!ID.equals(""))
            Results = Results.equalTo("ID", ID);
        if (!name.equals(""))
            Results = Results.equalTo("name", name);
        if (!memberID.equals(""))
            Results = Results.equalTo("memberID", memberID);
        if (!location.equals(""))
            Results = Results.equalTo("location", location);
        if (!parentImage.equals(""))
            Results = Results.equalTo("parentImage", parentImage);
        if(0 != dateBegin)
            Results = Results.greaterThanOrEqualTo("date",dateBegin);
        if(0 != dateEnd)
            Results = Results.lessThanOrEqualTo("date",dateEnd);
        List<PictureData> l = Manager.copyFromRealm(Results.equalTo("activate",true).findAll());
        return (null == l) ? new ArrayList<PictureData>(1) : l;
    }

    List<ThirdPartyAccountData> getThirdPartyAccountList(Realm Manager, String memberID, String account, int thirdPartyType) throws DataBaseError {
        RealmQuery<ThirdPartyAccountData> Results = Manager.where(ThirdPartyAccountData.class);
        if (!memberID.equals(""))
            Results = Results.equalTo("memberID", memberID);
        if (!account.equals(""))
            Results = Results.equalTo("account", account);
        if (0 != thirdPartyType)
            Results = Results.equalTo("thirdPartyType", thirdPartyType);
        List<ThirdPartyAccountData> l = Manager.copyFromRealm(Results.findAll());
        return (null == l) ? new ArrayList<ThirdPartyAccountData>(1) : l;
    }

    // Negative "amount" means no limitation
    List<PictureData> getRandomPicturesFromMember(Realm Manager, String memberID, String name, String location,
                                                  long dateBegin, long dateEnd, int amount)
            throws DataBaseError {
        try {
            PrimaryMemberIDCheck(Manager, memberID);
            RealmQuery<PictureData> Results = Manager.where(PictureData.class);
            if (!memberID.equals(""))
                Results = Results.equalTo("memberID", memberID);
            if (!name.equals(""))
                Results = Results.equalTo("name", name);
            if (!location.equals(""))
                Results = Results.equalTo("location", location);
            if (0 != dateBegin)
                Results = Results.greaterThanOrEqualTo("date", dateBegin);
            if (0 != dateEnd)
                Results = Results.lessThanOrEqualTo("date", dateEnd);

            List<PictureData> list = Manager.copyFromRealm(Results.equalTo("activate",true).findAll());
            if(amount >= 0) {
                if (list.size() < amount) {
                    Log.i("TAG", "getRandomPicturesFromMember : RequiredImageNotEnough");
                    throw new DataBaseError(DataBaseError.ErrorType.RequiredImageNotEnough);
                }
                Random rand = new Random();
                while (list.size() > amount) {
                    list.remove(rand.nextInt(list.size()) % list.size());
                }
            }
            return (null == list) ? new ArrayList<PictureData>(1) : list;
        } catch (DataBaseError e) {
            throw e;
        } catch (Exception e){
            Log.i("TAG", "getRandomPicturesFromMember : " + e.getMessage() + e.getLocalizedMessage());
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_PictureData);
        }
    }

    // Negative "amount" means no limitation
    List<PictureData> getRandomPicturesFromFamily(Realm Manager, String familyID, String name, String location,
                                                  long dateBegin, long dateEnd, int amount)
            throws DataBaseError {
        try {
            PrimaryFamilyIDCheck(Manager, familyID);
            List<MemberData> memberList = Manager.copyFromRealm(Manager.where(MemberData.class)
                    .equalTo("familyID",familyID).equalTo("activate",true).findAll());
            if(memberList.size() <= 0){
                Log.i("TAG", "getRandomPicturesFromFamily : MemberNotExist");
                throw new DataBaseError(DataBaseError.ErrorType.MemberNotExist);
            }

            // gathering family members
            List<PictureData> list = getRandomPicturesFromMember(Manager, memberList.get(0).getID(),
                    name, location, dateBegin, dateEnd, -1);
            for(int i=1;i<memberList.size();i++)
                list.addAll(getRandomPicturesFromMember(Manager, memberList.get(i).getID(),
                        name, location, dateBegin, dateEnd, -1));

            // select images
            if (amount >= 0) {
                if (list.size() < amount) {
                    Log.i("TAG", "getRandomPicturesFromFamily : RequiredImageNotEnough");
                    throw new DataBaseError(DataBaseError.ErrorType.RequiredImageNotEnough);
                }
                Random rand = new Random();
                while (list.size() > amount) {
                    list.remove(rand.nextInt(list.size()) % list.size());
                }
            }
            return (null == list) ? new ArrayList<PictureData>(1) : list;
        } catch (DataBaseError e) {
            throw e;
        } catch (Exception e) {
            Log.i("TAG", "getRandomPicturesFromFamily : " + e.getMessage() + e.getLocalizedMessage());
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_MemberData);
        }
    }

    void LoginCheck_Phone(Realm Manager, String phone, String password)
            throws DataBaseSignal, DataBaseError {
        RealmResults<PhoneData> Result = Manager.where(PhoneData.class).equalTo("phone", phone).findAll();
        if (Result.size() <= 0) {
            Log.i("TAG", "LoginCheck_Phone : MemberNotExist");
            throw new DataBaseError(DataBaseError.ErrorType.MemberNotExist);
        }
        else if (Result.size() > 1) {
            Log.i("TAG", "LoginCheck_Phone : PhoneNumberConflict");
            throw new DataBaseError(DataBaseError.ErrorType.PhoneNumberConflict);
        }
        RealmResults<MemberData> Match = Manager.where(MemberData.class)
                .equalTo("ID", Result.first().getMemberID())
                .equalTo("password", password).findAll();
        if (Match.size() <= 0) {
            Log.i("TAG", "LoginCheck_Phone : WrongLoginPassWord");
            throw new DataBaseError(DataBaseError.ErrorType.WrongLoginPassWord);
        }
        else if (Match.size() > 1) {
            Log.i("TAG", "LoginCheck_Phone : PhoneRedundant");
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_MemberData);
        }
        else if(!Match.first().getActivate()) {
            Log.i("TAG", "LoginCheck_Phone : MemberHibernating");
            throw new DataBaseError(DataBaseError.ErrorType.MemberHibernating);
        }
        throw new DataBaseSignal(DataBaseSignal.SignalType.LoginSucceed);
    }

    void LoginCheck_ThirdPartyAccount(Realm Manager, String account, int thirdPartyType, String password)
            throws DataBaseSignal, DataBaseError {
        RealmResults<ThirdPartyAccountData> Result = Manager.where(ThirdPartyAccountData.class)
                .equalTo("account", account).equalTo("thirdPartyType", thirdPartyType).findAll();
        if (Result.size() <= 0) {
            Log.i("TAG", "LoginCheck_ThirdPartyAccount : ThirdPartyAccountNotExist");
            throw new DataBaseError(DataBaseError.ErrorType.ThirdPartyAccountNotExist);
        }
        else if (Result.size() > 1) {
            Log.i("TAG", "LoginCheck_ThirdPartyAccount : ThirdPartyAccountConflict");
            throw new DataBaseError(DataBaseError.ErrorType.ThirdPartyAccountConflict);
        }
        RealmResults<MemberData> Match = Manager.where(MemberData.class)
                .equalTo("ID", Result.first().getMemberID())
                .equalTo("password", password).findAll();
        if (Match.size() <= 0) {
            Log.i("TAG", "LoginCheck_ThirdPartyAccount : WrongLoginPassWord");
            throw new DataBaseError(DataBaseError.ErrorType.WrongLoginPassWord);
        }
        else if (Match.size() > 1) {
            Log.i("TAG", "LoginCheck_ThirdPartyAccount : ThirdPartyAccountRedundant");
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_MemberData);
        }
        else if(!Match.first().getActivate()) {
            Log.i("TAG", "LoginCheck_ThirdPartyAccount : MemberHibernating");
            throw new DataBaseError(DataBaseError.ErrorType.MemberHibernating);
        }
        throw new DataBaseSignal(DataBaseSignal.SignalType.LoginSucceed);
    }

    void MemberHibernate(Realm Manager, String memberID)
            throws DataBaseError, DataBaseSignal {
        try {
            PrimaryMemberIDCheck(Manager, memberID);
            Manager.beginTransaction();
            MemberData updateMember = Manager.createObject(MemberData.class, memberID);
            updateMember.setActivate(false);
            Manager.commitTransaction();
            throw new DataBaseSignal(DataBaseSignal.SignalType.MemberHibernationSucceed);
        } catch (DataBaseError | DataBaseSignal e) {
            throw e;
        } catch(Exception e){
            Log.i("TAG", "MemberHibernate : " + e.getMessage() + e.getLocalizedMessage());
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_MemberData);
        }
    }

    void GameRecordHibernate(Realm Manager, String recordID)
            throws DataBaseError, DataBaseSignal {
        try {
            PrimaryRecordIDCheck(Manager, recordID);
            Manager.beginTransaction();
            GameRecordData updateRecord = Manager.createObject(GameRecordData.class, recordID);
            updateRecord.setActivate(false);
            Manager.commitTransaction();
            throw new DataBaseSignal(DataBaseSignal.SignalType.GameRecordHibernateSucceed);
        } catch (DataBaseError | DataBaseSignal e) {
            throw e;
        } catch(Exception e){
            Log.i("TAG", "GameRecordHibernate : " + e.getMessage() + e.getLocalizedMessage());
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_GameRecordData);
        }
    }

    void PictureHibernate(Realm Manager, String imageID)
            throws DataBaseError, DataBaseSignal {
        try {
            PrimaryPictureIDCheck(Manager, imageID);
            Manager.beginTransaction();
            PictureData updateImage = Manager.createObject(PictureData.class, imageID);
            updateImage.setActivate(false);
            Manager.commitTransaction();
            throw new DataBaseSignal(DataBaseSignal.SignalType.PictureHibernateSucceed);
        } catch (DataBaseError | DataBaseSignal e) {
            throw e;
        } catch(Exception e){
            Log.i("TAG", "PictureHibernate : " + e.getMessage() + e.getLocalizedMessage());
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_PictureData);
        }
    }

    void DestroyPhone(Realm Manager, String phone)
            throws DataBaseError, DataBaseSignal {
        try {
            RealmResults<PhoneData> Result = Manager.where(PhoneData.class).equalTo("phone", phone).findAll();
            if (Result.size() <= 0) {
                Log.i("TAG", "DestroyPhone : PhoneNotExist");
                throw new DataBaseError(DataBaseError.ErrorType.PhoneNotExist);
            }
            Manager.beginTransaction();
            PhoneData updateImage = Manager.createObject(PhoneData.class, phone);
            updateImage.deleteFromRealm();
            Manager.commitTransaction();
            throw new DataBaseSignal(DataBaseSignal.SignalType.PhoneDeleted);
        } catch (DataBaseError | DataBaseSignal e) {
            throw e;
        } catch(Exception e){
            Log.i("TAG", "DestroyPhone : " + e.getMessage() + e.getLocalizedMessage());
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_PhoneData);
        }
    }

    void DestroyThirdPartyAccount(Realm Manager, String memberID, int thirdPartyType, String account)
            throws DataBaseError, DataBaseSignal {
        try {
            PrimaryMemberIDCheck(Manager, memberID);
            RealmResults<ThirdPartyAccountData> Result = Manager.where(ThirdPartyAccountData.class)
                    .equalTo("memberID", memberID).equalTo("account", account)
                    .equalTo("thirdPartyType", thirdPartyType).findAll();
            if (Result.size() <= 0) {
                Log.i("TAG", "DestroyThirdPartyAccount : ThirdPartyAccountNotExist");
                throw new DataBaseError(DataBaseError.ErrorType.ThirdPartyAccountNotExist);
            }
            else if (Result.size() > 1) {
                Log.i("TAG", "DestroyThirdPartyAccount : ThirdPartyAccountConflict");
                throw new DataBaseError(DataBaseError.ErrorType.ThirdPartyAccountConflict);
            }
            Manager.beginTransaction();
            Result.first().deleteFromRealm();
            Manager.commitTransaction();
            throw new DataBaseSignal(DataBaseSignal.SignalType.OneThirdPartyAccountDeleted);
        } catch (DataBaseError | DataBaseSignal e) {
            throw e;
        } catch(Exception e){
            Log.i("TAG", "DestroyThirdPartyAccount : " + e.getMessage() + e.getLocalizedMessage());
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_ThirdPartyAccountData);
        }
    }

    void DestroyThirdPartyAccount(Realm Manager, String memberID, int thirdPartyType)
            throws DataBaseError, DataBaseSignal {
        try {
            PrimaryMemberIDCheck(Manager, memberID);
            RealmResults<ThirdPartyAccountData> Result = Manager.where(ThirdPartyAccountData.class)
                    .equalTo("memberID", memberID).equalTo("thirdPartyType", thirdPartyType).findAll();
            if (Result.size() <= 0) {
                Log.i("TAG", "DestroyThirdPartyAccount : ThirdPartyAccountNotExist");
                throw new DataBaseError(DataBaseError.ErrorType.ThirdPartyAccountNotExist);
            }
            int telomere = Result.size();
            while(Result.size() > 0 && --telomere >= 0){
                try{
                    DestroyThirdPartyAccount(Manager, memberID, thirdPartyType, Result.first().getAccount());
                } catch (DataBaseSignal s) {
                    if(DataBaseSignal.SignalType.OneThirdPartyAccountDeleted != s.getSignalType()) {
                        Log.i("TAG", "DestroyThirdPartyAccount : " + s.getSignalType() + s.getMessage() + s.getLocalizedMessage());
                        throw s;
                    }
                }
            }
            if(Result.size() > 0) {
                Log.i("TAG", "DestroyThirdPartyAccount : RealmResultAutoUpdateFail");
                throw new DataBaseError(DataBaseError.ErrorType.RealmResultAutoUpdateFail);
            }
            throw new DataBaseSignal(DataBaseSignal.SignalType.AllThirdPartyAccountDeleted);
        } catch (DataBaseError | DataBaseSignal e) {
            throw e;
        } catch(Exception e){
            Log.i("TAG", "DestroyThirdPartyAccount : " + e.getMessage() + e.getLocalizedMessage());
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_ThirdPartyAccountData);
        }
    }

    void DestroyRelation(Realm Manager, String memberA, String memberB)
            throws DataBaseError, DataBaseSignal{
        try {
            PrimaryMemberIDCheck(Manager, memberA);
            PrimaryMemberIDCheck(Manager, memberB);
            RealmResults<MemberRelationData> Result = Manager.where(MemberRelationData.class)
                    .equalTo("memberA", memberA).equalTo("memberB", memberB).findAll();
            if (Result.size() <= 0) {
                Result = Manager.where(MemberRelationData.class)
                        .equalTo("memberA", memberB).equalTo("memberB", memberA).findAll();
                if (Result.size() <= 0) {
                    Log.i("TAG", "DestroyRelation : MemberRelationNotExist");
                    throw new DataBaseError(DataBaseError.ErrorType.MemberRelationNotExist);
                }
            }
            if (Result.size() > 1) {
                Log.i("TAG", "DestroyRelation : MemberRelationRedundant");
                throw new DataBaseError(DataBaseError.ErrorType.UnknownError_MemberRelationData);
            }
            Manager.beginTransaction();
            Result.first().deleteFromRealm();
            Manager.commitTransaction();
            throw new DataBaseSignal(DataBaseSignal.SignalType.MemberRelationDeleted);
        } catch (DataBaseError | DataBaseSignal e) {
            throw e;
        } catch(Exception e){
            Log.i("TAG", "DestroyRelation : " + e.getMessage() + e.getLocalizedMessage());
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_MemberRelationData);
        }
    }

    void Divorce(Realm Manager, String memberA, String memberB)
            throws DataBaseError, DataBaseSignal{
        try {
            try {
                DestroyRelation(Manager, memberA, memberB);
            } catch (DataBaseSignal s){
                if(DataBaseSignal.SignalType.MemberRelationDeleted != s.getSignalType()) {
                    Log.i("TAG", "Divorce : " + s.getSignalType() + s.getMessage() + s.getLocalizedMessage());
                    throw s;
                }
            }
            AddMemberRelation(Manager, memberA, memberB, MemberRelationData.DB_RELATION_DIVORCE);
        } catch (DataBaseError | DataBaseSignal e) {
            throw e;
        } catch(Exception e){
            Log.i("TAG", "Divorce : " + e.getMessage() + e.getLocalizedMessage());
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_MemberRelationData);
        }
    }

    void UpdateFamily(Realm Manager, String ID, String name, String rootMemberID)
            throws DataBaseError, DataBaseSignal {
        try {
            PrimaryFamilyIDCheck(Manager, ID);
            if(!rootMemberID.equals("")) {
                PrimaryMemberIDCheck(Manager, rootMemberID);
                RealmResults<MemberData> newRootMember = Manager.where(MemberData.class)
                        .equalTo("ID", rootMemberID).findAll();
                if (!newRootMember.first().getFamilyID().equals(ID)) {
                    Log.i("TAG", "UpdateFamily : SettingStrangerAsFamilyRoot");
                    throw new DataBaseError(DataBaseError.ErrorType.SettingStrangerAsFamilyRoot);
                }
            }
            Manager.beginTransaction();
            RealmResults<FamilyData> Results = Manager.where(FamilyData.class)
                    .equalTo("ID", ID).equalTo("activate",true).findAll();
            if (Results.size() <= 0) {
                Log.i("TAG", "UpdateFamily : FamilyNotExist");
                throw new DataBaseError(DataBaseError.ErrorType.FamilyNotExist);
            }
            if(!name.equals(""))
                Results.first().setName(name);
            if(!rootMemberID.equals(""))
                Results.first().setRootMemberID(rootMemberID);
            Manager.commitTransaction();
            throw new DataBaseSignal(DataBaseSignal.SignalType.FamilyUpdated);
        } catch (DataBaseError | DataBaseSignal e) {
            throw e;
        } catch(Exception e){
            Log.i("TAG", "UpdateFamily : " + e.getMessage() + e.getLocalizedMessage());
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_FamilyData);
        }
    }

    // Negative "gender" means no changes
    void UpdateMember(Realm Manager, String ID, String password, int gender, String name, String nickname)
            throws DataBaseError, DataBaseSignal {
        try {
            PrimaryMemberIDCheck(Manager, ID);
            Manager.beginTransaction();
            RealmResults<MemberData> Results = Manager.where(MemberData.class)
                    .equalTo("ID", ID).equalTo("activate", true).findAll();
            if (!password.equals(""))
                Results.first().setPassword(password);
            if (gender > 0)
                Results.first().setGender(gender);
            if (!name.equals(""))
                Results.first().setName(name);
            if (!nickname.equals(""))
                Results.first().setNickName(nickname);
            Manager.commitTransaction();
            throw new DataBaseSignal(DataBaseSignal.SignalType.MemberUpdated);
        } catch (DataBaseError | DataBaseSignal e) {
            throw e;
        } catch(Exception e) {
            Log.i("TAG", "UpdateMember : " + e.getMessage() + e.getLocalizedMessage());
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_MemberData);
        }
    }

    // When "date" is 0, no changes will be made
    void UpdatePicture(Realm Manager, String ID, String imagePath, String name, String memberID,
                       String location, long date, String note, String parentImage)
            throws DataBaseError, DataBaseSignal {
        try {
            PrimaryPictureIDCheck(Manager, ID);
            if (!memberID.equals(""))
                PrimaryMemberIDCheck(Manager, memberID);
            if (!parentImage.equals("")) {
                try {
                    PrimaryPictureIDCheck(Manager, parentImage);
                } catch (DataBaseError e) {
                    if(DataBaseError.ErrorType.ImageNotExist == e.getErrorType()) {
                        Log.i("TAG", "UpdatePicture : ParentImageNotExist");
                        throw new DataBaseError(DataBaseError.ErrorType.ParentImageNotExist);
                    }
                    else {
                        Log.i("TAG", "UpdatePicture : " + e.getMessage() + e.getLocalizedMessage());
                        throw e;
                    }
                }
            }
            Manager.beginTransaction();
            RealmResults<PictureData> Results = Manager.where(PictureData.class)
                    .equalTo("ID", ID).equalTo("activate", true).findAll();
            if (!imagePath.equals(""))
                Results.first().setImagePath(imagePath);
            if (!name.equals(""))
                Results.first().setName(name);
            if (!memberID.equals(""))
                Results.first().setMemberID(memberID);
            if (!location.equals(""))
                Results.first().setLocation(location);
            if (0 != date)
                Results.first().setDate(date);
            if (!note.equals(""))
                Results.first().setNote(note);
            if (!parentImage.equals(""))
                Results.first().setParentImage(parentImage);
            Manager.commitTransaction();
            throw new DataBaseSignal(DataBaseSignal.SignalType.PictureUpdated);
        } catch (DataBaseError | DataBaseSignal e) {
            throw e;
        } catch(Exception e) {
            Log.i("TAG", "UpdatePicture : " + e.getMessage() + e.getLocalizedMessage());
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_PictureData);
        }
    }
}
