# Proposed timeline & task allocation

## Team member priorities
- Shayaan (auth & registration)
- Katie (events & search)
- Sahasra (bookings & cancellations)
- Leo (reviews, sponsors & integration)

---

## Task allocation — Group 47

---

### Shayaan — auth & registration

#### Model classes
- [ ] `User.java` — abstract base class with email, password, getters
- [ ] `Student.java` — extends User, includes phoneNumber, bookings list, preferences
- [ ] `AdminStaff.java` — extends User
- [ ] `EntertainmentProvider.java` — extends User, includes orgName, businessNumber, events list
- [ ] `StudentPreferences.java` — preferMusicEvents, preferTheaterEvents etc.

#### Controller
- [ ] `UserController.java` — implement `login()`, `logout()`, `registerEntertainmentProvider()`
- [ ] Add `editPreferences()` to `UserController.java`

#### Tests
- [ ] `LogInSystemTests.java`
- [ ] `LogOutSystemTests.java`
- [ ] `RegisterEntertainmentProviderSystemTests.java`
- [ ] Faculty Members unit tests

---
### Katie — events, search & EP actions

#### Model classes
- [ ] `Event.java` — eventID, title, type, isTicketed, performances list
- [ ] `Performance.java` — all fields from diagram, all methods from diagram
- [ ] `EventType.java` — enum: MUSIC, THEATRE, DANCE, MOVIE, SPORTS
- [ ] `PerformanceStatus.java` — enum: ACTIVE, CANCELLED

#### Controller
- [ ] `EventPerformanceController.java` — implement `createEvent()`, `searchForPerformances()`, `viewPerformance()`, `cancelPerformance()`, `sponsorPerformance()`
- [ ] Add `cancelPerformance()` method (written by Sahasra — paste it in)

#### Tests
- [ ] `TestMockPaymentSystem.java` — unit tests for MockPaymentSystem
- [ ] `CreateEventSystemTests.java`
- [ ] `SearchPerformancesSystemTests.java`
- [ ] `ViewPerformanceSystemTests.java`
- [ ] `CancelPerformanceSystemTests.java`
- [ ] `SponsorPerformanceSystemTests.java`

#### Faculty Members (eager migration — group 47 is odd)
- [ ] Implement Pre-register Faculty Members use case (separate project, not in main src)
- [ ] Create mock faculty file for testing

#### Critical — Performance.java must include these methods for everyone else's code to work:
- [ ] `checkIfEventIsTicketed()`
- [ ] `checkIfTicketsLeft(int n)`
- [ ] `getFinalTicketPrice()`
- [ ] `getOrganiserEmail()`
- [ ] `getEventTitle()`
- [ ] `getEventID()`
- [ ] `getPerformanceId()`
- [ ] `checkHasNotHappenedYet()`
- [ ] `hasActiveBookings()`
- [ ] `getBookingDetailsForRefund()`
- [ ] `cancel()`
- [ ] `checkCreatedByEP(String email)`
- [ ] `review(int rating, String comment)`
- [ ] `addBooking(Booking b)`
- [ ] `sponsor(double amount)`
---

### Sahasra — bookings & reviews

#### Model classes
- [ ] `Booking.java` ✓
- [ ] `BookingStatus.java` ✓ (ACTIVE, CANCELLEDBYSTUDENT, CANCELLEDBYPROVIDER, PAYMENTFAILED)

#### Controllers
- [ ] `Controller.java` ✓ — abstract base class
- [ ] `BookingController.java` ✓ — `bookPerformance()`, `cancelBooking()`, `reviewPerformance()`

#### Tests
- [ ] `TestBooking.java` — unit tests for Booking
- [ ] `BookPerformanceSystemTests.java`
- [ ] `EditPreferencesSystemTests.java`
- [ ] `CancelPerformanceSystemTests.java`
- [ ] Faculty Members system tests

---

### Leo — integration & report

#### Controller
- [ ] `MenuController.java` — main menu, route to correct controller based on user type

#### Tests
- [ ] Run full test suite and fix any integration issues
- [ ] `SponsorPerformanceSystemTests.java`
- [ ] `CancelBookingSystemTests.java`
- [ ] `ReviewPerformanceSystemTests.java`

#### Report
- [ ] Lead Task 5 reflection — teamwork (≤250 words)
- [ ] Lead Task 5 reflection — quality of work (≤250 words)
- [ ] Lead Task 5 reflection — tools & agile practices with screenshots (≤250 words)
- [ ] Write code review notes during the lab session

---

### Everyone

- [ ] Code review lab (must be before Mar 31)
- [ ] Javadoc & code quality


### Submission (Apr 6)
- [ ] export `CW3SEImplementationGroup47.zip` (no Faculty Members code)
- [ ] export `CW3SECodeReviewImplementationGroup47.zip` (Faculty Members only)
- [ ] export `CW3SEReportGroup47.pdf` (no names inside)
- [ ] export `CW3SEDeclarationGroup47.pdf` signed by all
> **Group number: 47 — eager migration for Faculty Members.**
---

## Timeline

### Week 1 — Mar 17–19: Setup
- [ ] All: clone repo, set up IntelliJ, install Checkstyle plugin
- [ ] All: read all provided class diagrams and sequence diagrams
- [ ] All: agree on method signatures for shared classes (User, EventsApp)

### Week 2 — Mar 20–26: Core implementation
- [ ] A: implement Log in, Log out, Register entertainment provider
- [ ] B: implement Create event, Search performances, View performance
- [ ] C: implement Book performance, Edit preferences, Cancel performance
- [ ] D: implement Cancel booking, Review performance, Sponsor performance

### Week 3 — Mar 24–28: Tests + Faculty Members (PRIORITY - needed for code review)
- [ ] A: unit tests for EventPerformance + system tests for own use cases
- [ ] B: unit tests for MockPaymentSystem + system tests for own use cases
- [ ] C: unit tests for Booking + system tests for own use cases
- [ ] D: system tests for own use cases + integration check (run full test suite)
- [ ] B leads: implement Pre-register Faculty Members — EAGER migration
- [ ] A + C: write unit and system tests for Faculty Members
- [ ] All: Faculty Members code must be fully done and tested by Mar 28

### Week 4 — Mar 28–31: Code review lab
- [ ] All: attend code review lab
- [ ] All: exchange Faculty Members code with paired group, write review notes
- [ ] All: submit code review notes on Learn during lab

### After Mar 31 — Apr 1–4: Final polishing
- [ ] D: write Task 5 reflections (teamwork, quality, tools)
- [ ] All: Javadoc pass — every public method must have a comment
- [ ] Run full test suite — everything must pass

### Final — Apr 5–6: Submission
- [ ] Export CW3SEImplementationGroupX.zip (no Faculty Members code)
- [ ] Export CW3SECodeReviewImplementationGroupX.zip (Faculty Members only)
- [ ] Write PDF report CW3SEReportGroupX.pdf
- [ ] Team Work Declaration signed by all — CW3SEDeclarationGroupX.pdf
- [ ] Each person submits Individual Work Declaration Form (by 12:30)

---

## Key dates

| Date            | Milestone                                                              |
|-----------------|------------------------------------------------------------------------|
| Mar 19          | Everyone set up and ready to code                                      |
| Mar 27          | All 12 use cases implemented, tests and Faculty Members implementation |                                           |
| Mar 31          | **Code review lab**                                                    |
| Mar 30          | All unit and system tests passing                                      |
| Apr 5           | Everything finalised and ready to submit                               |
| **Apr 6, 12:00** | **Deadline**                                                           |
| Apr 6, 12:30    | Declarations deadline                                                  |
---

