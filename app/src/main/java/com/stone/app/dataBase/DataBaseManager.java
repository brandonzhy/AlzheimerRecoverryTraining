package com.stone.app.dataBase;

import com.stone.app.dataBase.DataBaseError;
import com.stone.app.dataBase.DataBaseSignal;
import com.stone.app.dataBase.FamilyData;
import com.stone.app.dataBase.GameRecordData;
import com.stone.app.dataBase.MemberData;
import com.stone.app.dataBase.MemberRelationData;
import com.stone.app.dataBase.PhoneData;
import com.stone.app.dataBase.PictureData;
import com.stone.app.dataBase.ThirdPartyAccountData;

import java.util.List;
import java.util.Random;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class DataBaseManager {
    private Realm Manager;

    private void PrimaryMemberIDCheck(String memberID) throws DataBaseError {
        Manager = Realm.getDefaultInstance();
        RealmResults<MemberData> m = Manager.where(MemberData.class).equalTo("ID", memberID).findAll();
        if(m.size() <= 0)
            throw new DataBaseError(DataBaseError.ErrorType.MemberNotExist);
        else if(m.size() > 1)
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_MemberData);
        else if(!m.first().getActivate())
            throw new DataBaseError(DataBaseError.ErrorType.MemberHibernating);
        Manager = null;
    }

    private void PrimaryFamilyIDCheck(String familyID) throws DataBaseError {
        Manager = Realm.getDefaultInstance();
        RealmResults<FamilyData> f = Manager.where(FamilyData.class).equalTo("ID", familyID).findAll();
        if(f.size() <= 0)
            throw new DataBaseError(DataBaseError.ErrorType.FamilyNotExist);
        else if(f.size() > 1)
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_FamilyData);
        else if(!f.first().getActivate())
            throw new DataBaseError(DataBaseError.ErrorType.FamilyHibernating);
        Manager = null;
    }

    private void PrimaryRecordIDCheck(String recordID) throws DataBaseError {
        Manager = Realm.getDefaultInstance();
        RealmResults<GameRecordData> r = Manager.where(GameRecordData.class).equalTo("recordID", recordID).findAll();
        if(r.size() <= 0)
            throw new DataBaseError(DataBaseError.ErrorType.RecordNotExist);
        else if(r.size() > 1)
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_GameRecordData);
        else if(!r.first().getActivate())
            throw new DataBaseError(DataBaseError.ErrorType.RecordHibernating);
        Manager = null;
    }

    private void PrimaryPictureIDCheck(String pictureID) throws DataBaseError {
        Manager = Realm.getDefaultInstance();
        RealmResults<PictureData> p = Manager.where(PictureData.class).equalTo("ID", pictureID).findAll();
        if(p.size() <= 0)
            throw new DataBaseError(DataBaseError.ErrorType.ImageNotExist);
        else if(p.size() > 1)
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_PictureData);
        else if(!p.first().getActivate())
            throw new DataBaseError(DataBaseError.ErrorType.PictureHibernating);
        Manager = null;
    }

    // Migrate relation on B to A
    private void AutoTransplanting(String memberA, String memberB)
            throws DataBaseError, DataBaseSignal{
        try{
            PrimaryMemberIDCheck(memberA);
            PrimaryMemberIDCheck(memberB);
            Manager = Realm.getDefaultInstance();

            // Combine original family
            MemberData memA = getMemberList(memberA,"","","").get(0);
            MemberData memB = getMemberList(memberB,"","","").get(0);
            if(!memA.getFamilyID().equals(memB.getFamilyID())) {
                try {
                    AddExistMemberToExistFamily(memberB, memA.getFamilyID());
                } catch (DataBaseSignal s) {
                    if(DataBaseSignal.SignalType.AddSingleMemberToFamilySucceed != s.getSignalType()
                            && DataBaseSignal.SignalType.MergeTwoFamiliesSucceed != s.getSignalType())
                        throw s;
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
                        throw new DataBaseError(DataBaseError.ErrorType.RelationError_AlloTransplanting);
                    }
                    else{
                        autoTransplant = true;
                        try {
                            DestroyRelation(memberA, memberB);
                        } catch (DataBaseSignal s){
                            if(DataBaseSignal.SignalType.MemberRelationDeleted != s.getSignalType())
                                throw s;
                        }
                    }
                }
                // Relation "(memberB, member)" is to transplant
                else if(memberB.equals(m.getMemberA())){
                    String member = m.getMemberB();
                    int relation = m.getRelation();
                    try {
                        DestroyRelation(memberB, member);
                    } catch (DataBaseSignal s){
                        if(DataBaseSignal.SignalType.MemberRelationDeleted != s.getSignalType())
                            throw s;
                    }
                    List<MemberRelationData> list = getMemberRelationList(member, relation);
                    for(int i=list.size()-1;i>=0;i--){
                        if(memberA.equals(list.get(i).getMemberA()) || memberA.equals(list.get(i).getMemberB()))
                            break;
                        if(0 == i){
                            try{
                                AddMemberRelation(memberA, member, relation);
                            } catch (DataBaseSignal s){
                                if(DataBaseSignal.SignalType.MemberRelationAddedAlready != s.getSignalType())
                                    throw s;
                            }
                        }
                    }
                }
                // Relation "(member, memberB)" is to transplant
                else {
                    String member = m.getMemberA();
                    int relation = m.getRelation();
                    try {
                        DestroyRelation(member, memberB);
                    } catch (DataBaseSignal s){
                        if(DataBaseSignal.SignalType.MemberRelationDeleted != s.getSignalType())
                            throw s;
                    }
                    List<MemberRelationData> list = getMemberRelationList(member, relation);
                    for(int i=list.size()-1;i>=0;i--){
                        if(memberA.equals(list.get(i).getMemberA()) || memberA.equals(list.get(i).getMemberB()))
                            break;
                        if(0 == i){
                            try{
                                AddMemberRelation(member, memberA, relation);
                            } catch (DataBaseSignal s){
                                if(DataBaseSignal.SignalType.MemberRelationAddedAlready != s.getSignalType())
                                    throw s;
                            }
                        }
                    }
                }
            }
            if(Result.size() > 0)
                throw new DataBaseError(DataBaseError.ErrorType.RealmResultAutoUpdateFail);

            Manager = null;
            // Re-construct oneself relation
            if(!autoTransplant)
                throw new DataBaseError(DataBaseError.ErrorType.RelationError_UnknownTransplanting);
            else{
                try{
                    AddMemberRelation(memberA, memberB, MemberRelationData.DB_RELATION_ONESELF);
                } catch(DataBaseSignal s) {
                    if(DataBaseSignal.SignalType.MemberRelationAddedAlready == s.getSignalType())
                        throw new DataBaseSignal(DataBaseSignal.SignalType.AutoTransplantationSucceed);
                }
            }
        } catch (DataBaseError | DataBaseSignal e) {
            throw e;
        } catch (Exception e) {
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_AutoTransplanting);
        }
    }

    // Requires healthy tree
    private String FindSiblingListHead(String member) throws DataBaseError {
        try {
            PrimaryMemberIDCheck(member);
            Manager = Realm.getDefaultInstance();
            List<MemberRelationData> siblingList = getMemberRelationList(member, MemberRelationData.DB_RELATION_SIBLING);
            MemberRelationData relation;

            for (int i = 0; i < siblingList.size(); i++) {
                relation = siblingList.get(i);
                if (member.equals(relation.getMemberB()))
                    return FindSiblingListHead(relation.getMemberA());
            }
            Manager = null;
            return member;
        } catch (DataBaseError e) {
            throw e;
        } catch (Exception e) {
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_FindSiblingListHead);
        }
    }

    // Requires healthy tree
    private String FindSiblingListTail(String member) throws DataBaseError {
        try {
            PrimaryMemberIDCheck(member);
            Manager = Realm.getDefaultInstance();
            List<MemberRelationData> siblingList = getMemberRelationList(member, MemberRelationData.DB_RELATION_SIBLING);
            MemberRelationData relation;

            for (int i = 0; i < siblingList.size(); i++) {
                relation = siblingList.get(i);
                if (member.equals(relation.getMemberA()))
                    return FindSiblingListTail(relation.getMemberB());
            }
            Manager = null;
            return member;
        } catch (DataBaseError e) {
            throw e;
        } catch (Exception e) {
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_FindSiblingListTail);
        }
    }

    // Count the distance between two people, return -1 means unlinked
    public int Stadiometry(String memberA, String memberB) throws DataBaseError {
        try {
            PrimaryMemberIDCheck(memberA);
            PrimaryMemberIDCheck(memberB);
            int dist = -1;
            Manager = Realm.getDefaultInstance();



            Manager = null;
            return dist;
        } catch (DataBaseError e) {
            throw e;
        } catch (Exception e) {
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_FindSiblingListTail);
        }
    }

    // Translate relation between two members into words
    public String Transcription(String memberA, String memberB) throws DataBaseError {
        try {
            PrimaryMemberIDCheck(memberA);
            PrimaryMemberIDCheck(memberB);
            String appellation = "";
            Manager = Realm.getDefaultInstance();



            Manager = null;
            return appellation;
        } catch (DataBaseError e) {
            throw e;
        } catch (Exception e) {
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_FindSiblingListTail);
        }
    }

    // Only lazy deletion is safe, or ID conflict will occur
    public FamilyData AddFamily(String name, String memberID)
            throws DataBaseError {
        try {
            Manager = Realm.getDefaultInstance();
            RealmResults<MemberData> Result = Manager.where(MemberData.class)
                    .equalTo("ID",memberID).findAll();
            if(!memberID.equals("")){
                if(Result.size() <= 0)
                    throw new DataBaseError(DataBaseError.ErrorType.MemberNotExist);
                else if(Result.size() > 1)
                    throw new DataBaseError(DataBaseError.ErrorType.UnknownError_MemberData);
                else if(!Result.first().getFamilyID().equals(""))
                    throw new DataBaseError(DataBaseError.ErrorType.MemberHasFamilyAlready);
                else if(!Result.first().getActivate())
                    throw new DataBaseError(DataBaseError.ErrorType.MemberHibernating);
            }

            Manager.beginTransaction();
            FamilyData newFamily = Manager.createObject(FamilyData.class);
            newFamily.setID(String.valueOf(Manager.where(FamilyData.class).findAll().size()));
            newFamily.setName(name);
            newFamily.setRootMemberID(memberID);
            newFamily.setActivate(true);
            Manager.commitTransaction();

            // Family ID update for member must be after completion of family
            Manager.beginTransaction();
            if(!memberID.equals(""))
                Result.first().setFamilyID(newFamily.getID());
            Manager.commitTransaction();
            Manager = null;
            return newFamily;
        } catch (DataBaseError e) {
            throw e;
        } catch (Exception e) {
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_AddFamily);
        }
    }

    public MemberData AddMember(String nickname, String password, String familyID,
                                String name, int gender, String phone)
            throws DataBaseError {
        try {
            Manager = Realm.getDefaultInstance();
            RealmResults<FamilyData> Result = Manager.where(FamilyData.class)
                    .equalTo("ID",familyID).findAll();
            if(!familyID.equals("")){
                if(Result.size() <= 0)
                    throw new DataBaseError(DataBaseError.ErrorType.FamilyNotExist);
                else if(Result.size() > 1)
                    throw new DataBaseError(DataBaseError.ErrorType.UnknownError_FamilyData);
                else if(!Result.first().getActivate())
                    throw new DataBaseError(DataBaseError.ErrorType.FamilyHibernating);
            }

            // add member info
            Manager.beginTransaction();
            MemberData newMember = Manager.createObject(MemberData.class);
            newMember.setID(String.valueOf(Manager.where(MemberData.class).findAll().size()));
            newMember.setFamilyID(familyID);
            newMember.setName(name);
            newMember.setGender(gender);
            newMember.setNickName(nickname);
            newMember.setPassword(password);
            newMember.setActivate(true);
            Manager.commitTransaction();

            // Root Member ID update for family must be after completion of member
            Manager.beginTransaction();
            if(Result.first().getRootMemberID().equals(""))
                Result.first().setRootMemberID(newMember.getID());
            Manager.commitTransaction();

            // add phone info
            try {
                AddPhone(phone, newMember.getID());
            } catch (DataBaseSignal s) {
                if(DataBaseSignal.SignalType.PhoneAddedAlready != s.getSignalType())
                    throw s;
            } catch (DataBaseError e) {
                throw e;
            } catch (Exception e) {
                throw new DataBaseError(DataBaseError.ErrorType.UnknownError_AddPhone);
            }

            Manager = null;
            return newMember;
        } catch (DataBaseError e) {
            throw e;
        } catch (Exception e) {
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_AddMember);
        }
    }

    public void AddExistMemberToExistFamily(String familyID, String memberID)
            throws DataBaseError, DataBaseSignal {
        try {
            PrimaryMemberIDCheck(memberID);
            PrimaryFamilyIDCheck(familyID);
            Manager = Realm.getDefaultInstance();

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
                if(ResultM.size() > 0)
                    throw new DataBaseError(DataBaseError.ErrorType.RealmResultAutoUpdateFail);
                single = false;
            }
            else single = true;
            Manager.commitTransaction();
            Manager = null;
            if(single) throw new DataBaseSignal(DataBaseSignal.SignalType.AddSingleMemberToFamilySucceed);
            else throw new DataBaseSignal(DataBaseSignal.SignalType.MergeTwoFamiliesSucceed);
        } catch (DataBaseError | DataBaseSignal e) {
            throw e;
        } catch(Exception e){
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_MemberData);
        }
    }

    public void AddPhone(String phone, String memberID)
            throws DataBaseSignal, DataBaseError {
        try {
            PrimaryMemberIDCheck(memberID);
            Manager = Realm.getDefaultInstance();
            RealmResults<PhoneData> Result = Manager.where(PhoneData.class).equalTo("phone", phone).findAll();
            if (Result.size() > 0)
                throw new DataBaseError(DataBaseError.ErrorType.PhoneNumberConflict);

            Manager.beginTransaction();
            PhoneData newPhone = Manager.createObject(PhoneData.class);
            newPhone.setMemberID(memberID);
            newPhone.setPhone(phone);
            Manager.commitTransaction();
            Manager = null;
            throw new DataBaseSignal(DataBaseSignal.SignalType.PhoneAddedAlready);
        } catch (DataBaseError | DataBaseSignal e) {
            throw e;
        } catch (Exception e) {
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_AddPhone);
        }
    }

    public void AddGameRecord(String memberID, double factor, long date, int gameType)
            throws DataBaseSignal, DataBaseError {
        try {
            PrimaryMemberIDCheck(memberID);
            Manager = Realm.getDefaultInstance();
            Manager.beginTransaction();
            GameRecordData newRecord = Manager.createObject(GameRecordData.class);
            newRecord.setRecordID(String.valueOf(Manager.where(GameRecordData.class).findAll().size()));
            newRecord.setFactor(factor);
            newRecord.setGameType(gameType);
            newRecord.setMemberID(memberID);
            newRecord.setDate(date);
            newRecord.setActivate(true);
            Manager.commitTransaction();
            Manager = null;
            throw new DataBaseSignal(DataBaseSignal.SignalType.GameRecordAddedAlready);
        } catch (DataBaseError | DataBaseSignal e) {
            throw e;
        } catch (Exception e) {
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_AddGameRecord);
        }
    }

    // Attention : A is parent to B / A is hasband and B is wife
    public void AddMemberRelation(String memberA, String memberB, int relation)
            throws DataBaseSignal, DataBaseError {
        try {
            PrimaryMemberIDCheck(memberA);
            PrimaryMemberIDCheck(memberB);
            if(Stadiometry(memberA, memberB) >= 0)
                throw new DataBaseError(DataBaseError.ErrorType.RelationError_TryingToCombineTwoLinkedTree);
            Manager = Realm.getDefaultInstance();

            // Reach sibling list terminal
            if(MemberRelationData.DB_RELATION_SIBLING == relation) {
                memberA = FindSiblingListTail(memberA);
                memberB = FindSiblingListHead(memberB);
            }
            else if(MemberRelationData.DB_RELATION_PARENT == relation) {
                memberB = FindSiblingListHead(memberB);
            }

            // Relation Check
            List<MemberRelationData> listA = getMemberRelationList(memberA, 0);
            List<MemberRelationData> listB = getMemberRelationList(memberB, 0);
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

            // First deal with A-related relations
            for(int i=0;i<listA.size();i++){
                m = listA.get(i);
                String member = memberA.equals(m.getMemberA()) ? m.getMemberB() : m.getMemberA();

                // A and B has relation already
                if(memberB.equals(member)) {
                    throw new DataBaseError(DataBaseError.ErrorType.RelationError_Redundant);
                }

                // Standardize oneself relation
                else if(MemberRelationData.DB_RELATION_ONESELF == m.getRelation()
                        && getMemberRelationList(member,0).size() > 1){
                    try {
                        AutoTransplanting(memberA, member);
                    } catch (DataBaseSignal s) {
                        if(DataBaseSignal.SignalType.AutoTransplantationSucceed != s.getSignalType())
                            throw s;
                    }
                    // Recursion : Refresh the lists and re-do adding operation
                    // It will break by throwing a signal
                    AddMemberRelation(memberA, memberB, relation);
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
                            throw new DataBaseError(DataBaseError.ErrorType.RelationError_Blood_TwoChildStructure);
                        }
                        // Transfer from double child to child-sibling structure
                        // Note : Change relation-adding method here, it will break by throwing a signal
                        else if(MemberRelationData.DB_RELATION_PARENT == relation){
                            AddMemberRelation(
                                    FindSiblingListTail(member),
                                    FindSiblingListHead(memberB),
                                    MemberRelationData.DB_RELATION_SIBLING);
                        }
                    }
                    // When A has a parent
                    // Original error : 2-parent error
                    else if(++parentA > 1) {
                        throw new DataBaseError(DataBaseError.ErrorType.RelationError_Blood_TwoParentStructure);
                    }
                    // Original error : mixed 3-blood error
                    else if (parentA + siblingA > 2) {
                        throw new DataBaseError(DataBaseError.ErrorType.RelationError_Blood_MixedThreeBloodStructure);
                    }
                    // Transferring due to mixed 2-blood structure
                    // Note : Change relation-adding method here, it will break by throwing a signal
                    else if(siblingA > 0 && MemberRelationData.DB_RELATION_SIBLING == relation){
                        while(MemberRelationData.DB_RELATION_SIBLING != m.getRelation())
                            m = listA.get(--i);
                        member = memberA.equals(m.getMemberA()) ? m.getMemberB() : m.getMemberA();
                        AddMemberRelation(
                                FindSiblingListTail(member),
                                FindSiblingListHead(memberB),
                                MemberRelationData.DB_RELATION_SIBLING);
                    }
                }
                // When A has a spouse
                else if(MemberRelationData.DB_RELATION_SPOUSE == m.getRelation()){
                    // Error : Conflict spouses
                    if(++maritalA > 1 || MemberRelationData.DB_RELATION_SPOUSE == relation) {
                        throw new DataBaseError(DataBaseError.ErrorType.RelationError_Bigamy);
                    }
                }
                // When A has a sibling
                else if(MemberRelationData.DB_RELATION_SIBLING == m.getRelation()) {
                    // Original error : 3-sibling error
                    if (++siblingA > 2) {
                        throw new DataBaseError(DataBaseError.ErrorType.RelationError_Blood_ThreeSiblingStructure);
                    }
                    // Original error : mixed 3-blood error
                    else if (parentA + siblingA > 2) {
                        throw new DataBaseError(DataBaseError.ErrorType.RelationError_Blood_MixedThreeBloodStructure);
                    }
                    // Transferring due to mixed 2-blood structure
                    // Note : Change relation-adding method here, it will break by throwing a signal
                    else if (parentA + siblingA > 1 && MemberRelationData.DB_RELATION_SIBLING == relation) {
                        AddMemberRelation(
                                FindSiblingListTail(member),
                                FindSiblingListHead(memberB),
                                MemberRelationData.DB_RELATION_SIBLING);
                    }
                }
            }

            // Then deal with B-related relations
            for(int i=0;i<listB.size();i++){
                m = listB.get(i);
                String member = memberB.equals(m.getMemberA()) ? m.getMemberB() : m.getMemberA();

                // A and B has relation already
                if(memberA.equals(member)) {
                    throw new DataBaseError(DataBaseError.ErrorType.RelationError_Redundant);
                }

                // Standardize oneself relation
                else if(MemberRelationData.DB_RELATION_ONESELF == m.getRelation()
                        && getMemberRelationList(member,0).size() > 1){
                    try {
                        AutoTransplanting(memberB, member);
                    } catch (DataBaseSignal s) {
                        if(DataBaseSignal.SignalType.AutoTransplantationSucceed != s.getSignalType())
                            throw s;
                    }
                    // Recursion : Refresh the lists and re-do adding operation
                    // It will break by throwing a signal
                    AddMemberRelation(memberA, memberB, relation);
                }

                // When B has a parent / child
                else if(MemberRelationData.DB_RELATION_PARENT == m.getRelation()){
                    // When B has a child
                    if(memberB.equals(m.getMemberA())) {
                        // Original error : 2-child error
                        if (++childB > 1) {
                            throw new DataBaseError(DataBaseError.ErrorType.RelationError_Blood_TwoChildStructure);
                        }
                        // LABEL : Spouse_SeparateParentFromSiblingStructure
                        // Pre-address in case of separate-parent-from-sibling structure
                        else if(childA > 0 && MemberRelationData.DB_RELATION_SPOUSE == relation){
                            try{
                                DestroyRelation(member, memberB);
                            } catch (DataBaseSignal s) {
                                if(DataBaseSignal.SignalType.MemberRelationDeleted != s.getSignalType()){
                                    throw s;
                                }
                            }
                            try{
                                AddMemberRelation(
                                        FindSiblingListTail(ChildIDA),
                                        FindSiblingListHead(member),
                                        MemberRelationData.DB_RELATION_SIBLING);
                            } catch (DataBaseSignal s) {
                                if(DataBaseSignal.SignalType.MemberRelationAddedAlready != s.getSignalType()){
                                    throw s;
                                }
                            }
                            childB--;
                        }
                    }
                    // When B has a parent
                    // Original error : 2-parent error
                    else if(++parentB > 1){
                        throw new DataBaseError(DataBaseError.ErrorType.RelationError_Blood_TwoParentStructure);
                    }
                    // Original error : mixed 3-blood error
                    if(parentB + siblingB > 2) {
                        throw new DataBaseError(DataBaseError.ErrorType.RelationError_Blood_MixedThreeBloodStructure);
                    }
                    // Transfer from double parent to parent-spouse structure
                    // Note : Change relation-adding method here, it will break by throwing a signal
                    else if(MemberRelationData.DB_RELATION_PARENT == relation){
                        AddMemberRelation(member, memberA, MemberRelationData.DB_RELATION_SPOUSE);
                    }
                    // LABEL : Sibling_SeparateParentFromSiblingStructure
                    // When A and B are siblings, B's parent must be linked to A so that only one parent will link
                    else if(MemberRelationData.DB_RELATION_SIBLING == relation){
                        // Cut B and its parent
                        try {
                            DestroyRelation(member, memberB);
                        } catch (DataBaseSignal s) {
                            if(DataBaseSignal.SignalType.MemberRelationDeleted != s.getSignalType()) {
                                throw s;
                            }
                        }
                        // A married B's parent
                        if(parentA > 0) {
                            try {
                                AddMemberRelation(ParentIDA, member, MemberRelationData.DB_RELATION_SPOUSE);
                            } catch (DataBaseSignal s) {
                                if(DataBaseSignal.SignalType.MemberRelationAddedAlready != s.getSignalType()) {
                                    throw s;
                                }
                            }
                        }
                        // A got a parent
                        else {
                            try {
                                AddMemberRelation(member, memberA, MemberRelationData.DB_RELATION_PARENT);
                            } catch (DataBaseSignal s) {
                                if (DataBaseSignal.SignalType.MemberRelationAddedAlready != s.getSignalType()) {
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
                        throw new DataBaseError(DataBaseError.ErrorType.RelationError_Bigamy);
                    }
                }
                // When B has a sibling
                else if(MemberRelationData.DB_RELATION_SIBLING == m.getRelation()) {
                    // Original error : 3-sibling error
                    if (++siblingB > 2) {
                        throw new DataBaseError(DataBaseError.ErrorType.RelationError_Blood_ThreeSiblingStructure);
                    }
                    // Original error : mixed 3-blood error
                    else if (parentB + siblingB > 2 || parentB + siblingB > 1 && MemberRelationData.DB_RELATION_SIBLING == relation) {
                        throw new DataBaseError(DataBaseError.ErrorType.RelationError_Blood_MixedThreeBloodStructure);
                    }
                }
            }

            // Final congenital drawbacks check
            // Error : Conflict spouses
            if (maritalA > 1 || maritalB > 1 || maritalA + maritalB > 0 && MemberRelationData.DB_RELATION_SPOUSE == relation) {
                throw new DataBaseError(DataBaseError.ErrorType.RelationError_Bigamy);
            }
            // Original error : 3-sibling error
            if (siblingA > 2 || siblingB > 2 || (siblingA > 1 || siblingB > 1) && MemberRelationData.DB_RELATION_SIBLING == relation) {
                throw new DataBaseError(DataBaseError.ErrorType.RelationError_Blood_ThreeSiblingStructure);
            }
            // Original error : 2-parent error
            if (parentA > 1 || parentB > 1 || parentB > 0 && MemberRelationData.DB_RELATION_PARENT == relation) {
                throw new DataBaseError(DataBaseError.ErrorType.RelationError_Blood_TwoParentStructure);
            }
            // Original error : 2-child error
            if (childA > 1 || childB > 1 || childA > 0 && MemberRelationData.DB_RELATION_PARENT == relation) {
                throw new DataBaseError(DataBaseError.ErrorType.RelationError_Blood_TwoChildStructure);
            }
            // Original error : mixed 3-blood error
            if (parentA + siblingA > 2 || parentB + siblingB > 2) {
                throw new DataBaseError(DataBaseError.ErrorType.RelationError_Blood_MixedThreeBloodStructure);
            }

            // Spouse check
            if (MemberRelationData.DB_RELATION_SPOUSE == relation) {
                List<MemberData> memA = getMemberList(memberA, "", "", "");
                List<MemberData> memB = getMemberList(memberB, "", "", "");
                int genderA = memA.get(0).getGender();
                int genderB = memB.get(0).getGender();
                if (genderA == genderB && (genderA == MemberData.DB_GENDER_MALE || genderB == MemberData.DB_GENDER_FEMALE)) {
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
                    throw new DataBaseError(DataBaseError.ErrorType.RelationError_Blood_SeparateParentFromSiblingStructure);
                }
            }
            // Parent check
            if (MemberRelationData.DB_RELATION_PARENT == relation) {
                if(parentA + siblingA > 1 || parentB + siblingB > 1) {
                    throw new DataBaseError(DataBaseError.ErrorType.RelationError_Blood_MixedThreeBloodStructure);
                }
            }
            // Sibling check
            // Note : SeparateParentFromSiblingStructure Problem will be addressed after sibling list is built
            if (MemberRelationData.DB_RELATION_SIBLING == relation) {
                if(parentA + siblingA > 1 || parentB + siblingB > 1) {
                    throw new DataBaseError(DataBaseError.ErrorType.RelationError_Blood_MixedThreeBloodStructure);
                }
                // It should be addressed at LABEL : "Sibling_SeparateParentFromSiblingStructure"
                if(parentA > 0 && parentB > 0) {
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
            Manager = null;
            throw new DataBaseSignal(DataBaseSignal.SignalType.MemberRelationAddedAlready);
        } catch (DataBaseError | DataBaseSignal e) {
            throw e;
        } catch (Exception e) {
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_AddRelation);
        }
    }

    public void AddImage(String memberID, String name, String location, String imagePath,
                         long date, String note, String parentImage)
            throws DataBaseSignal, DataBaseError {
        try {
            PrimaryMemberIDCheck(memberID);
            if (!parentImage.equals("")) {
                try {
                    PrimaryPictureIDCheck(parentImage);
                } catch (DataBaseError e) {
                    if(DataBaseError.ErrorType.ImageNotExist == e.getErrorType())
                        throw new DataBaseError(DataBaseError.ErrorType.ParentImageNotExist);
                    else throw e;
                }
            }

            Manager = Realm.getDefaultInstance();
            Manager.beginTransaction();
            PictureData newImage = Manager.createObject(PictureData.class);
            newImage.setID(String.valueOf(Manager.where(PictureData.class).findAll().size()));
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
            Manager = null;
            throw new DataBaseSignal(DataBaseSignal.SignalType.ImageAddedAlready);
        } catch (DataBaseError | DataBaseSignal e) {
            throw e;
        } catch (Exception e) {
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_AddImage);
        }
    }

    public void AddThirdPartyAccount(String memberID, String account, int thirdPartyType)
            throws DataBaseSignal, DataBaseError {
        try {
            PrimaryMemberIDCheck(memberID);
            Manager = Realm.getDefaultInstance();
            RealmResults<ThirdPartyAccountData> Result = Manager.where(ThirdPartyAccountData.class)
                    .equalTo("account", account).equalTo("thirdPartyType", thirdPartyType).findAll();
            if (Result.size() > 0)
                throw new DataBaseError(DataBaseError.ErrorType.ThirdPartyAccountConflict);

            Manager.beginTransaction();
            ThirdPartyAccountData newAccount = Manager.createObject(ThirdPartyAccountData.class);
            newAccount.setMemberID(memberID);
            newAccount.setAccount(account);
            newAccount.setThirdPartyType(thirdPartyType);
            Manager.commitTransaction();
            Manager = null;
            throw new DataBaseSignal(DataBaseSignal.SignalType.ThirdPartyAccountAddedAlready);
        } catch (DataBaseError | DataBaseSignal e) {
            throw e;
        } catch (Exception e) {
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_AddThirdPartyAccount);
        }
    }

    public List<PhoneData> getPhoneList(String phone, String memberID) {
        Manager = Realm.getDefaultInstance();
        RealmQuery<PhoneData> Results = Manager.where(PhoneData.class);
        if (!phone.equals(""))
            Results = Results.equalTo("phone", phone);
        if (!memberID.equals(""))
            Results = Results.equalTo("memberID", memberID);
        Manager = null;
        return Manager.copyFromRealm(Results.findAll());
    }

    public List<FamilyData> getFamilyList(String ID, String name, String rootMemberID) {
        Manager = Realm.getDefaultInstance();
        RealmQuery<FamilyData> Results = Manager.where(FamilyData.class);
        if (!ID.equals(""))
            Results = Results.equalTo("ID", ID);
        if (!name.equals(""))
            Results = Results.equalTo("name", name);
        if (!rootMemberID.equals(""))
            Results = Results.equalTo("rootMemberID", rootMemberID);
        Manager = null;
        return Manager.copyFromRealm(Results.equalTo("activate",true).findAll());
    }

    public List<MemberData> getMemberList(String ID, String familyID, String name, String nickname) {
        Manager = Realm.getDefaultInstance();
        RealmQuery<MemberData> Results = Manager.where(MemberData.class);
        if (!ID.equals(""))
            Results = Results.equalTo("ID", ID);
        if (!familyID.equals(""))
            Results = Results.equalTo("familyID", familyID);
        if (!name.equals(""))
            Results = Results.equalTo("name", name);
        if (!nickname.equals(""))
            Results = Results.equalTo("nickname", nickname);
        Manager = null;
        return Manager.copyFromRealm(Results.equalTo("activate",true).findAll());
    }

    // Note : Member could be inactive; relation "0" means no restriction
    public List<MemberRelationData> getMemberRelationList(String member, int relation) {
        Manager = Realm.getDefaultInstance();
        RealmQuery<MemberRelationData> Results = Manager.where(MemberRelationData.class);
        if (!member.equals(""))
            Results = Results.equalTo("memberA", member).or().equalTo("memberB", member);
        if(0 != relation)
            Results = Results.equalTo("relation",relation);
        Manager = null;
        return Manager.copyFromRealm(Results.findAll());
    }

    public List<GameRecordData> getGameRecordList(String recordID, String memberID, int gameType,
                                                  long dateBegin, long dateEnd) {
        Manager = Realm.getDefaultInstance();
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
        Manager = null;
        return Manager.copyFromRealm(Results.equalTo("activate",true).findAll());
    }

    public List<PictureData> getPictureList(String ID, String name, String memberID,
                                            String location, String parentImage,
                                            long dateBegin, long dateEnd) {
        Manager = Realm.getDefaultInstance();
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
        Manager = null;
        return Manager.copyFromRealm(Results.equalTo("activate",true).findAll());
    }

    public List<ThirdPartyAccountData> getThirdPartyAccountList(String memberID, String account, int thirdPartyType) {
        Manager = Realm.getDefaultInstance();
        RealmQuery<ThirdPartyAccountData> Results = Manager.where(ThirdPartyAccountData.class);
        if (!memberID.equals(""))
            Results = Results.equalTo("memberID", memberID);
        if (!account.equals(""))
            Results = Results.equalTo("account", account);
        if (0 != thirdPartyType)
            Results = Results.equalTo("thirdPartyType", thirdPartyType);
        Manager = null;
        return Manager.copyFromRealm(Results.findAll());
    }

    // Negative "amount" means no limitation
    public List<PictureData> getRandomPicturesFromMember(String memberID, String name, String location,
                                                         long dateBegin, long dateEnd, int amount)
            throws DataBaseError {
        try {
            PrimaryMemberIDCheck(memberID);
            Manager = Realm.getDefaultInstance();
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
                if (list.size() < amount)
                    throw new DataBaseError(DataBaseError.ErrorType.RequiredImageNotEnough);
                Random rand = new Random();
                while (list.size() > amount) {
                    list.remove(rand.nextInt(list.size()) % list.size());
                }
            }
            Manager = null;
            return list;
        } catch (DataBaseError e) {
            throw e;
        } catch (Exception e){
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_PictureData);
        }
    }

    // Negative "amount" means no limitation
    public List<PictureData> getRandomPicturesFromFamily(String familyID, String name, String location,
                                                         long dateBegin, long dateEnd, int amount)
            throws DataBaseError {
        try {
            PrimaryFamilyIDCheck(familyID);
            Manager = Realm.getDefaultInstance();
            List<MemberData> memberList = Manager.copyFromRealm(Manager.where(MemberData.class)
                    .equalTo("familyID",familyID).equalTo("activate",true).findAll());
            if(memberList.size() <= 0)
                throw new DataBaseError(DataBaseError.ErrorType.MemberNotExist);

            // gathering family members
            List<PictureData> list = getRandomPicturesFromMember(memberList.get(0).getID(),
                    name, location, dateBegin, dateEnd, -1);
            for(int i=1;i<memberList.size();i++)
                list.addAll(getRandomPicturesFromMember(memberList.get(i).getID(),
                        name, location, dateBegin, dateEnd, -1));

            // select images
            if (amount >= 0) {
                if (list.size() < amount)
                    throw new DataBaseError(DataBaseError.ErrorType.RequiredImageNotEnough);
                Random rand = new Random();
                while (list.size() > amount) {
                    list.remove(rand.nextInt(list.size()) % list.size());
                }
            }
            Manager = null;
            return list;
        } catch (DataBaseError e) {
            throw e;
        } catch (Exception e) {
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_MemberData);
        }
    }

    public void LoginCheck_Phone(String phone, String password)
            throws DataBaseSignal, DataBaseError {
        Manager = Realm.getDefaultInstance();
        RealmResults<PhoneData> Result = Manager.where(PhoneData.class).equalTo("phone", phone).findAll();
        if (Result.size() <= 0)
            throw new DataBaseError(DataBaseError.ErrorType.MemberNotExist);
        else if (Result.size() > 1)
            throw new DataBaseError(DataBaseError.ErrorType.PhoneNumberConflict);
        RealmResults<MemberData> Match = Manager.where(MemberData.class)
                .equalTo("ID", Result.first().getMemberID())
                .equalTo("password", password).findAll();
        if (Match.size() <= 0)
            throw new DataBaseError(DataBaseError.ErrorType.WrongLoginPassWord);
        else if (Match.size() > 1)
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_MemberData);
        else if(!Match.first().getActivate())
            throw new DataBaseError(DataBaseError.ErrorType.MemberHibernating);
        Manager = null;
        throw new DataBaseSignal(DataBaseSignal.SignalType.LoginSucceed);
    }

    public void LoginCheck_ThirdPartyAccount(String account, int thirdPartyType, String password)
            throws DataBaseSignal, DataBaseError {
        Manager = Realm.getDefaultInstance();
        RealmResults<ThirdPartyAccountData> Result = Manager.where(ThirdPartyAccountData.class)
                .equalTo("account", account).equalTo("thirdPartyType", thirdPartyType).findAll();
        if (Result.size() <= 0)
            throw new DataBaseError(DataBaseError.ErrorType.ThirdPartyAccountNotExist);
        else if (Result.size() > 1)
            throw new DataBaseError(DataBaseError.ErrorType.ThirdPartyAccountConflict);
        RealmResults<MemberData> Match = Manager.where(MemberData.class)
                .equalTo("ID", Result.first().getMemberID())
                .equalTo("password", password).findAll();
        if (Match.size() <= 0)
            throw new DataBaseError(DataBaseError.ErrorType.WrongLoginPassWord);
        else if (Match.size() > 1)
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_MemberData);
        else if(!Match.first().getActivate())
            throw new DataBaseError(DataBaseError.ErrorType.MemberHibernating);
        Manager = null;
        throw new DataBaseSignal(DataBaseSignal.SignalType.LoginSucceed);
    }

    public void MemberHibernate(String memberID)
            throws DataBaseError, DataBaseSignal {
        try {
            PrimaryMemberIDCheck(memberID);
            Manager = Realm.getDefaultInstance();
            Manager.beginTransaction();
            MemberData updateMember = Manager.createObject(MemberData.class, memberID);
            updateMember.setActivate(false);
            Manager.commitTransaction();
            Manager = null;
            throw new DataBaseSignal(DataBaseSignal.SignalType.MemberHibernationSucceed);
        } catch (DataBaseError | DataBaseSignal e) {
            throw e;
        } catch(Exception e){
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_MemberData);
        }
    }

    public void GameRecordHibernate(String recordID)
            throws DataBaseError, DataBaseSignal {
        try {
            PrimaryRecordIDCheck(recordID);
            Manager = Realm.getDefaultInstance();
            Manager.beginTransaction();
            GameRecordData updateRecord = Manager.createObject(GameRecordData.class, recordID);
            updateRecord.setActivate(false);
            Manager.commitTransaction();
            Manager = null;
            throw new DataBaseSignal(DataBaseSignal.SignalType.GameRecordHibernateSucceed);
        } catch (DataBaseError | DataBaseSignal e) {
            throw e;
        } catch(Exception e){
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_GameRecordData);
        }
    }

    public void PictureHibernate(String imageID)
            throws DataBaseError, DataBaseSignal {
        try {
            PrimaryPictureIDCheck(imageID);
            Manager = Realm.getDefaultInstance();
            Manager.beginTransaction();
            PictureData updateImage = Manager.createObject(PictureData.class, imageID);
            updateImage.setActivate(false);
            Manager.commitTransaction();
            Manager = null;
            throw new DataBaseSignal(DataBaseSignal.SignalType.PictureHibernateSucceed);
        } catch (DataBaseError | DataBaseSignal e) {
            throw e;
        } catch(Exception e){
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_PictureData);
        }
    }

    public void DestroyPhone(String phone)
            throws DataBaseError, DataBaseSignal {
        try {
            Manager = Realm.getDefaultInstance();
            RealmResults<PhoneData> Result = Manager.where(PhoneData.class).equalTo("phone", phone).findAll();
            if (Result.size() <= 0)
                throw new DataBaseError(DataBaseError.ErrorType.PhoneNotExist);
            Manager.beginTransaction();
            PhoneData updateImage = Manager.createObject(PhoneData.class, phone);
            updateImage.deleteFromRealm();
            Manager.commitTransaction();
            Manager = null;
            throw new DataBaseSignal(DataBaseSignal.SignalType.PhoneDeleted);
        } catch (DataBaseError | DataBaseSignal e) {
            throw e;
        } catch(Exception e){
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_PhoneData);
        }
    }

    public void DestroyThirdPartyAccount(String memberID, int thirdPartyType, String account)
            throws DataBaseError, DataBaseSignal {
        try {
            PrimaryMemberIDCheck(memberID);
            Manager = Realm.getDefaultInstance();
            RealmResults<ThirdPartyAccountData> Result = Manager.where(ThirdPartyAccountData.class)
                    .equalTo("memberID", memberID).equalTo("account", account)
                    .equalTo("thirdPartyType", thirdPartyType).findAll();
            if (Result.size() <= 0)
                throw new DataBaseError(DataBaseError.ErrorType.ThirdPartyAccountNotExist);
            else if (Result.size() > 1)
                throw new DataBaseError(DataBaseError.ErrorType.ThirdPartyAccountConflict);
            Manager.beginTransaction();
            Result.first().deleteFromRealm();
            Manager.commitTransaction();
            Manager = null;
            throw new DataBaseSignal(DataBaseSignal.SignalType.OneThirdPartyAccountDeleted);
        } catch (DataBaseError | DataBaseSignal e) {
            throw e;
        } catch(Exception e){
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_ThirdPartyAccountData);
        }
    }

    public void DestroyThirdPartyAccount(String memberID, int thirdPartyType)
            throws DataBaseError, DataBaseSignal {
        try {
            PrimaryMemberIDCheck(memberID);
            Manager = Realm.getDefaultInstance();
            RealmResults<ThirdPartyAccountData> Result = Manager.where(ThirdPartyAccountData.class)
                    .equalTo("memberID", memberID).equalTo("thirdPartyType", thirdPartyType).findAll();
            if (Result.size() <= 0)
                throw new DataBaseError(DataBaseError.ErrorType.ThirdPartyAccountNotExist);
            int telomere = Result.size();
            while(Result.size() > 0 && --telomere >= 0){
                try{
                    DestroyThirdPartyAccount(memberID, thirdPartyType, Result.first().getAccount());
                } catch (DataBaseSignal s) {
                    if(DataBaseSignal.SignalType.OneThirdPartyAccountDeleted != s.getSignalType())
                        throw s;
                }
            }
            if(Result.size() > 0)
                throw new DataBaseError(DataBaseError.ErrorType.RealmResultAutoUpdateFail);
            Manager = null;
            throw new DataBaseSignal(DataBaseSignal.SignalType.AllThirdPartyAccountDeleted);
        } catch (DataBaseError | DataBaseSignal e) {
            throw e;
        } catch(Exception e){
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_ThirdPartyAccountData);
        }
    }

    public void DestroyRelation(String memberA, String memberB)
            throws DataBaseError, DataBaseSignal{
        try {
            PrimaryMemberIDCheck(memberA);
            PrimaryMemberIDCheck(memberB);
            Manager = Realm.getDefaultInstance();
            RealmResults<MemberRelationData> Result = Manager.where(MemberRelationData.class)
                    .equalTo("memberA", memberA).equalTo("memberB", memberB).findAll();
            if (Result.size() <= 0) {
                Result = Manager.where(MemberRelationData.class)
                        .equalTo("memberA", memberB).equalTo("memberB", memberA).findAll();
                if (Result.size() <= 0)
                    throw new DataBaseError(DataBaseError.ErrorType.MemberRelationNotExist);
            }
            if (Result.size() > 1)
                throw new DataBaseError(DataBaseError.ErrorType.UnknownError_MemberRelationData);
            Manager.beginTransaction();
            Result.first().deleteFromRealm();
            Manager.commitTransaction();
            Manager = null;
            throw new DataBaseSignal(DataBaseSignal.SignalType.MemberRelationDeleted);
        } catch (DataBaseError | DataBaseSignal e) {
            throw e;
        } catch(Exception e){
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_MemberRelationData);
        }
    }

    public void Divorce(String memberA, String memberB)
            throws DataBaseError, DataBaseSignal{
        try {
            try {
                DestroyRelation(memberA, memberB);
            } catch (DataBaseSignal s){
                if(DataBaseSignal.SignalType.MemberRelationDeleted != s.getSignalType())
                    throw s;
            }
            AddMemberRelation(memberA, memberB, MemberRelationData.DB_RELATION_DIVORCE);
        } catch (DataBaseError | DataBaseSignal e) {
            throw e;
        } catch(Exception e){
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_MemberRelationData);
        }
    }

    public void UpdateFamily(String ID, String name, String rootMemberID)
            throws DataBaseError, DataBaseSignal {
        try {
            PrimaryFamilyIDCheck(ID);
            if(!rootMemberID.equals("")) {
                PrimaryMemberIDCheck(rootMemberID);
                Manager = Realm.getDefaultInstance();
                RealmResults<MemberData> newRootMember = Manager.where(MemberData.class)
                        .equalTo("ID", rootMemberID).findAll();
                if (!newRootMember.first().getFamilyID().equals(ID))
                    throw new DataBaseError(DataBaseError.ErrorType.SettingStrangerAsFamilyRoot);
                Manager = null;
            }

            Manager = Realm.getDefaultInstance();
            Manager.beginTransaction();
            RealmResults<FamilyData> Results = Manager.where(FamilyData.class)
                    .equalTo("ID", ID).equalTo("activate",true).findAll();
            if (Results.size() <= 0)
                throw new DataBaseError(DataBaseError.ErrorType.FamilyNotExist);
            if(!name.equals(""))
                Results.first().setName(name);
            if(!rootMemberID.equals(""))
                Results.first().setRootMemberID(rootMemberID);
            Manager.commitTransaction();
            Manager = null;
            throw new DataBaseSignal(DataBaseSignal.SignalType.FamilyUpdated);
        } catch (DataBaseError | DataBaseSignal e) {
            throw e;
        } catch(Exception e){
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_FamilyData);
        }
    }

    // Negative "gender" means no changes
    public void UpdateMember(String ID, String password, int gender, String name, String nickname)
            throws DataBaseError, DataBaseSignal {
        try {
            PrimaryMemberIDCheck(ID);
            Manager = Realm.getDefaultInstance();
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
            Manager = null;
            throw new DataBaseSignal(DataBaseSignal.SignalType.MemberUpdated);
        } catch (DataBaseError | DataBaseSignal e) {
            throw e;
        } catch(Exception e) {
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_MemberData);
        }
    }

    // When "date" is 0, no changes will be made
    public void UpdatePicture(String ID, String imagePath, String name, String memberID,
                              String location, long date, String note, String parentImage)
            throws DataBaseError, DataBaseSignal {
        try {
            PrimaryPictureIDCheck(ID);
            if (!memberID.equals(""))
                PrimaryMemberIDCheck(memberID);
            if (!parentImage.equals("")) {
                try {
                    PrimaryPictureIDCheck(parentImage);
                } catch (DataBaseError e) {
                    if(DataBaseError.ErrorType.ImageNotExist == e.getErrorType())
                        throw new DataBaseError(DataBaseError.ErrorType.ParentImageNotExist);
                    else throw e;
                }
            }

            Manager = Realm.getDefaultInstance();
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
            Manager = null;
            throw new DataBaseSignal(DataBaseSignal.SignalType.MemberUpdated);
        } catch (DataBaseError | DataBaseSignal e) {
            throw e;
        } catch(Exception e) {
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_PictureData);
        }
    }
}
