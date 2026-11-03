package com.Sk.BookMyShow.Services;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.Sk.BookMyShow.Entity.Screen;
import com.Sk.BookMyShow.Entity.Seat;
import com.Sk.BookMyShow.Entity.Theatre;
import com.Sk.BookMyShow.Entity.Deleted_Entity.DeletedSeat;
import com.Sk.BookMyShow.Enums.SeatStatus;
import com.Sk.BookMyShow.Enums.SeatType;
import com.Sk.BookMyShow.Exceptions.ResourceNotFoundException;
import com.Sk.BookMyShow.Repositories.ScreenRepo;
import com.Sk.BookMyShow.Repositories.SeatRepo;
import com.Sk.BookMyShow.Repositories.TheatreRepo;
import com.Sk.BookMyShow.Repositories.Deleted_data_Repo.DeletedSeatRepo;
import com.Sk.BookMyShow.Utils.SecurityUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SeatService {

    private final SeatRepo seatRepo;
    private final ScreenRepo screenRepo;
    private final TheatreRepo theatreRepo;
    private final DeletedSeatRepo deletedSeatRepo;

    void validateScreenAndTheatre(String screenId, String theatreId) {
        // 1) Screen exist karti hai?
        Screen screen = screenRepo.findById(new ObjectId(screenId))
                .orElseThrow(() -> new ResourceNotFoundException("Invalid Screen Id"));

        // 2) Screen isi theatre ki hai?
        if (!screen.getTheatreId().equals(theatreId)) {
            throw new ResourceNotFoundException("Screen does not belong to this theatre");
        }

        // 3) Theatre exist karta hai?
        Theatre theatre = theatreRepo.findById(new ObjectId(theatreId))
                .orElseThrow(() -> new ResourceNotFoundException("Theatre not Exist"));

        // 4) Logged-in user hi owner hai?
        if (!SecurityUtils.getCurrentLoggedUserId().equals(theatre.getOwnerId())) {
            throw new AccessDeniedException("You can't delete seats of someone else's theatre");
        }

    }

    public List<Seat> addRows(int startingRowIdx, int endingRow, int startingColIdx, int endingColumn,
            String screenId) {

        return restoreOrCreateSeats(startingRowIdx, endingRow, startingColIdx, endingColumn, screenId);

    }

    public List<Seat> addCols(int startingRowIdx, int endingRow, int startingColIdx, int endingColumn,
            String screenId) {

        return restoreOrCreateSeats(startingRowIdx, endingRow, startingColIdx, endingColumn, screenId);

    }

    private List<Seat> restoreOrCreateSeats(int startingRow, int endingRow, int startingColumn, int endingCol,
            String screenId) {

        List<Seat> seats = seatRepo.findSeatsInRange(screenId, startingRow, endingRow, startingColumn, endingCol);
        HashSet<String> existingKeys = new HashSet<>();

        Iterator<Seat> iterator = seats.iterator();

        while (iterator.hasNext()) {
            Seat seat = iterator.next();
            existingKeys.add(seat.getRowIndex() + "-" + seat.getColumnIndex());
            if (seat.getStatus() == SeatStatus.REMOVED)
                seat.setStatus(SeatStatus.ACTIVE);

        }

        for (int i = startingRow; i < endingRow; i++) {

            String rowLabel = "Row " + String.valueOf(i + 1);

            for (int j = startingColumn; j < endingCol; j++) {
                if (existingKeys.contains(i + "-" + j))
                    continue;
                seats.add(Seat.builder()
                        .screenId(screenId)
                        .rowIndex(i)
                        .columnIndex(j)
                        .rowLabel(rowLabel)
                        .seatNumber(j + 1)
                        .seatLabel(String.valueOf(i) + String.valueOf(j))
                        .seatType(SeatType.REGULAR)
                        .status(SeatStatus.ACTIVE)
                        .build());
            }

        }

        return seatRepo.saveAll(seats);
    }

    public List<Seat> removeSeats(int startingRow, int endingRow, int startingColumn, int endingCol, String screenId) {

        List<Seat> seats = seatRepo.findActivateSeats(screenId, startingRow, endingRow, startingColumn, endingCol);
        seats.forEach(seat -> seat.setStatus(SeatStatus.REMOVED));

        return seatRepo.saveAll(seats);
    }

    public List<Seat> updateSeats(int newRows, int newCol, Screen oldScreen) {

        int oldRows = oldScreen.getTotalRows();
        int oldCols = oldScreen.getTotalColumns();

        String screenId = String.valueOf(oldScreen.getId());

        List<Seat> updatedSeats = new ArrayList<>();

        // 1. Rows increase hui hain
        if (newRows > oldRows) {

            updatedSeats.addAll(
                    addRows(oldRows, newRows, 0, newCol, screenId));
        }

        // 2. Columns increase hue hain
        if (newCol > oldCols) {

            updatedSeats.addAll(
                    addCols(0, Math.min(oldRows, newRows),
                            oldCols, newCol, screenId));
        }

        // 3. Rows decrease hui hain
        if (newRows < oldRows) {

            updatedSeats.addAll(
                    removeSeats(newRows, oldRows, 0, oldCols, screenId));
        }

        // 4. Columns decrease hue hain
        if (newCol < oldCols) {

            updatedSeats.addAll(
                    removeSeats(0, Math.min(oldRows, newRows),
                            newCol, oldCols, screenId));
        }

        return updatedSeats;
    }

    public List<Seat> deactivateSeats(String theatreId, String screenId) {

        // 5) Is screen ki saari ACTIVE seats nikalo aur REMOVED mark karo
        List<Seat> seats = seatRepo.findByScreenId(screenId).stream()
                .filter(seat -> seat.getStatus() == SeatStatus.ACTIVE)
                .toList();

        seats.forEach(seat -> seat.setStatus(SeatStatus.REMOVED));

        return seatRepo.saveAll(seats);
    }

    public void deleteSeats(String screenId) {

          // Step 1: Screen ki saari seats find karo
    List<Seat> seats = seatRepo.findByScreenId(screenId);

    if (seats.isEmpty()) {
        return;
    }

    // Step 2: Har seat ka archive record banao
    List<DeletedSeat> deletedSeats = seats.stream()
            .map(seat -> {
                DeletedSeat deleted = new DeletedSeat();
                seat.setStatus(SeatStatus.REMOVED);
                deleted.setOriginalSeatId(String.valueOf(seat.getId()));
                deleted.setSeatData(seat);
                return deleted;
            })
            .toList();

    // Step 3: Archive collection mein save karo
    deletedSeatRepo.saveAll(deletedSeats);

    // Step 4: Original seats delete karo
    seatRepo.deleteByScreenId(screenId);
    }

    public List<Seat> getAllSeats(String screenId){
        return seatRepo.findByScreenId(screenId);
    }

}
