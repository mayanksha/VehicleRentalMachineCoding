package com.navi.repository;

import com.navi.exception.DuplicateEntityException;
import com.navi.exception.EntityNotFoundException;
import com.navi.model.BookingToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookingRepository {
    private final Map<String, BookingToken> db = new HashMap<>();

    public BookingToken getBookingTokenForId(final String ID) throws EntityNotFoundException {
        if (!db.containsKey(ID)) {
            throw new EntityNotFoundException(BookingToken.class.getName(), ID);
        }

        return db.get(ID);
    }

    public void insertBookingForId(final String ID, final BookingToken token) throws DuplicateEntityException {
        if (db.containsKey(ID)) {
            throw new DuplicateEntityException(BookingToken.class.getName(), ID);
        }

        db.put(ID, token);
    }

    public List<BookingToken> getAllTokensForVehicleID(final String vehicleID) {
        List<BookingToken> tokens = new ArrayList<>();

        for (Map.Entry<String, BookingToken> entry : db.entrySet()) {
            if (entry.getValue().getVehicleID().equals(vehicleID)) {
                tokens.add(entry.getValue());
            }
        }

        return tokens;
    }

//    public List<BookingToken> getAllTokensForVehicleIDOutOfTimeRange(final String vehicleID, Integer start, Integer end) {
//        assert (start < end);
//        List<BookingToken> tokens = new ArrayList<>();
//
//        for (Map.Entry<String, BookingToken> entry : db.entrySet()) {
//            if (entry.getValue().getVehicleID().equals(vehicleID)) {
//                if (end <= entry.getValue().getStartTime() || start >= entry.getValue().getEndTime())
//                    tokens.add(entry.getValue());
//            }
//        }
//
//        return tokens;
//    }

    private static BookingRepository instance;

    public static BookingRepository getInstance() {
        if (instance == null)
            instance = new BookingRepository();
        return instance;
    }

    private BookingRepository() {

    }

}
