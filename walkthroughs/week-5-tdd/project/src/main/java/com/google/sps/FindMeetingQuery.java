// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/*
Scenario: Imagine you're working on the calendar team 
and are responsible for adding the "find a meeting" feature.
Using the existing API, you'll need to implement a feature 
that given the meeting information, it will return the times 
when the meeting could happen that day.
*/
package com.google.sps;

import java.util.Collection;
import java.util.Collections;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet; 

public final class FindMeetingQuery {

  private int start;
  private int end;

  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    //edge cases

      //if there are no attendees, the whole day is available
      Collection<String> requestAttendees = request.getAttendees();
      if (requestAttendees.size() == 0) {
          return Arrays.asList(TimeRange.WHOLE_DAY);
      }

      //if duration is longer than a day, none of the day is available
      long requestDuration = request.getDuration();
      if (requestDuration > TimeRange.WHOLE_DAY.duration()) {
          return Arrays.asList();
      }
    
    // list of unavailable times
    ArrayList<TimeRange> meetingTimes = new ArrayList<TimeRange>();

    //list of available times
    ArrayList<TimeRange> availableTimes = new ArrayList<TimeRange>();

    /* nested loop : for each event, go through attendees. if attendee is in 
    the collection of event attendees, add to array of meeting times */
    for (Event e : events) {
        Collection<String> eventAttendees = e.getAttendees();
        for (String a : requestAttendees) {
            if (eventAttendees.contains(a)) {
                meetingTimes.add(e.getWhen());
            }
        }
    }
  
    //if there are no events for any attendees, the whole day is available
    if (meetingTimes.size() == 0) {
        return Arrays.asList(TimeRange.WHOLE_DAY);
    }

    ArrayList<TimeRange> unavailable = resolveOverlap(meetingTimes);
    
    Collections.sort(unavailable, TimeRange.ORDER_BY_START);
    TimeRange firstSlot = TimeRange.fromStartEnd(TimeRange.START_OF_DAY, unavailable.get(0).start(), false);

    if (firstSlot.duration() >= requestDuration) {
        availableTimes.add(firstSlot);
    }

    /* end of event = start of avaiable time
        start of event = end of available time */

    TimeRange freeTime;

    for (int i = 0; i < unavailable.size(); i++) {

        //get start
        start = unavailable.get(i).end();

        //get end, if at the last meeting, set to end of day
        if (i == unavailable.size() - 1) {
            end = TimeRange.END_OF_DAY + 1;
        } else {
            end = unavailable.get(i + 1).start();
        }

        freeTime = TimeRange.fromStartEnd(start, end, false);
        
        
        //check if request duration fits in freetime
        if (freeTime.duration() >= requestDuration) {
            availableTimes.add(freeTime);
        }
      }
    return availableTimes;
  }

  private ArrayList<TimeRange> resolveOverlap(ArrayList<TimeRange> meetingTimes) {
    TimeRange curr;
    TimeRange next;

    //use set(index, new value) to replace values in arraylist
    //remove(index) to remove values in arraylist

    //combine overlapping events
    for (int i = 0; i < meetingTimes.size(); i++) {
        curr = meetingTimes.get(i);

        if (i == meetingTimes.size() - 1) {
            next = TimeRange.fromStartEnd(TimeRange.END_OF_DAY, TimeRange.END_OF_DAY, true);
        } else {
            next = meetingTimes.get(i + 1);
        }

        if (next.overlaps(curr)) {
            if (curr.contains(next)) {
              meetingTimes.remove(i + 1);
            } else if (curr.end() >= next.start()) {
              start = curr.start();
              end = next.end();
              TimeRange newRange = TimeRange.fromStartEnd(start, end, false);
              meetingTimes.set(i, newRange);
              meetingTimes.remove(i + 1);

            }
        }
    }
  return meetingTimes;
  }
}
