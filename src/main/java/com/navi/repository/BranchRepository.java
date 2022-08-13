package com.navi.repository;

import com.navi.exception.DuplicateEntityException;
import com.navi.exception.EntityNotFoundException;
import com.navi.model.Branch;

import java.util.HashMap;
import java.util.Map;

public class BranchRepository {
    private final Map<String, Branch> db = new HashMap<>();

    public Branch getBranchForId(String ID) throws EntityNotFoundException {
        if (!db.containsKey(ID)) {
            throw new EntityNotFoundException(Branch.class.getName(), ID);
        }

        return db.get(ID);
    }

    public void insertBranchForId(String ID, Branch branch) throws DuplicateEntityException {
        if (db.containsKey(ID)) {
            throw new DuplicateEntityException(Branch.class.getName(), ID);
        }

        db.put(ID, branch);
    }

    private static BranchRepository instance;

    public static BranchRepository getInstance() {
        if (instance == null)
            instance = new BranchRepository();
        return instance;
    }

    private BranchRepository() {

    }

}
