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
- [ ] Create `User.java` (abstract base class with email, password, getters)
- [ ] Create `Student.java` (extends User)
- [ ] Create `Staff.java` (extends User)
- [ ] Create `EntertainmentProvider.java` (extends User)
- [ ] Create `ConsumerPreferences.java`

#### EventsApp methods
- [ ] Create initial `EventsApp.java` skeleton (fields, constructor, hardcoded student/staff accounts)
- [ ] Implement `logIn()`
- [ ] Implement `logOut()`
- [ ] Implement `registerEntertainmentProvider()`

#### Tests
- [ ] `TestEventPerformance.java` — unit tests for EventPerformance
- [ ] `LogInSystemTests.java`
- [ ] `LogOutSystemTests.java`
- [ ] `RegisterEntertainmentProviderSystemTests.java`
- [ ] Faculty Members unit tests

---

### Katie— events & search

#### Model classes
- [ ] Create `Event.java`
- [ ] Create `EventPerformance.java`

#### EventsApp methods
- [ ] Implement `createEvent()`
- [ ] Implement `addEventPerformance()`
- [ ] Implement `searchPerformances()`
- [ ] Implement `getEventPerformance()`

#### Tests
- [ ] `TestMockPaymentSystem.java` — unit tests for MockPaymentSystem
- [ ] `CreateEventSystemTests.java`
- [ ] `SearchPerformancesSystemTests.java`
- [ ] `ViewPerformanceSystemTests.java`

#### Faculty Members (eager migration — group 47 is odd)
- [ ] Implement Pre-register Faculty Members use case (kept separate, not in main src)
- [ ] Create mock faculty file for testing purposes

---

### Sahasra — bookings & cancellations

#### Model classes
- [ ] Create `Booking.java`
- [ ] Create `BookingStatus.java` (enum: ACTIVE, CANCELLED_BY_CONSUMER, CANCELLED_BY_PROVIDER)

#### EventsApp methods
- [ ] Implement `bookPerformance()`
- [ ] Implement `editPreferences()`
- [ ] Implement `cancelPerformance()`

#### Tests
- [ ] `TestBooking.java` — unit tests for Booking
- [ ] `BookPerformanceSystemTests.java`
- [ ] `EditPreferencesSystemTests.java`
- [ ] `CancelPerformanceSystemTests.java`
- [ ] Faculty Members system tests

---

### Leo — reviews, sponsors & integration

#### Model classes
- [ ] Create `Review.java`
- [ ] Create `Sponsorship.java`

#### EventsApp methods
- [ ] Implement `cancelBooking()`
- [ ] Implement `reviewPerformance()`
- [ ] Implement `sponsorPerformance()`

#### Tests
- [ ] `CancelBookingSystemTests.java`
- [ ] `ReviewPerformanceSystemTests.java`
- [ ] `SponsorPerformanceSystemTests.java`

#### Integration & report
- [ ] Run full test suite and fix any integration issues
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

