
       IDENTIFICATION DIVISION.
       PROGRAM-ID. CUSTREG.

       ENVIRONMENT DIVISION.
       INPUT-OUTPUT SECTION.
       FILE-CONTROL.
           SELECT CUSTOMER-FILE ASSIGN TO "CUSTOMER.dat"
           ORGANIZATION IS LINE SEQUENTIAL.

       DATA DIVISION.
       FILE SECTION.
       FD CUSTOMER-FILE.
       01 CUSTOMER-RECORD.
           05 CR-ID              PIC 9(5).
           05 CR-NAME            PIC X(30).
           05 CR-EMAIL           PIC X(50).
           05 CR-BIRTHDATE       PIC 9(8).

       WORKING-STORAGE SECTION.
       01 WS-CUSTOMER-ID     PIC 9(5) VALUE 00001.
       01 WS-NAME            PIC X(30).
       01 WS-EMAIL           PIC X(50).
       01 WS-BIRTHDATE       PIC 9(8).
       01 WS-BIRTH-DATE-NUM  REDEFINES WS-BIRTHDATE PIC 9(8).
       01 WS-ACTION          PIC X VALUE SPACE.
       01 WS-EOF             PIC X VALUE "N".
       01 WS-FOUND           PIC X VALUE "N".
       01 WS-CUSTOMER-TABLE.
          05 WS-CUSTOMER-ENTRY OCCURS 100 TIMES INDEXED BY WS-IDX.
             10 WS-CR-ID              PIC 9(5).
             10 WS-CR-NAME            PIC X(30).
             10 WS-CR-EMAIL           PIC X(50).
             10 WS-CR-BIRTHDATE       PIC 9(8).
       01 WS-CUSTOMER-COUNT       PIC 9(3) VALUE 0.
       01 WS-VALID-DATA       PIC X VALUE "Y".
       01 WS-ERROR-MESSAGE    PIC X(50).
       01 WS-CURRENT-DATE.
          05 WS-CURRENT-YEAR  PIC 9(4).
          05 WS-CURRENT-MONTH PIC 9(2).
          05 WS-CURRENT-DAY   PIC 9(2).
       01 WS-BIRTH-COMPONENTS.
          05 WS-BIRTH-YEAR    PIC 9(4).
          05 WS-BIRTH-MONTH   PIC 9(2).
          05 WS-BIRTH-DAY     PIC 9(2).
       01 WS-DAYS-IN-MONTH    PIC 9(2).
       01 WS-LEAP-YEAR        PIC X VALUE "N".
       01 WS-TEMP-NUM         PIC 9(8).
       01 WS-NUMERIC-TEST     PIC X.
          88 IS-NUMERIC       VALUE "Y".
          88 IS-NOT-NUMERIC   VALUE "N".
       01 WS-AT-COUNT         PIC 9(2) VALUE 0.
       01 WS-DOT-COUNT        PIC 9(2) VALUE 0.
       01 WS-HAS-SPACE        PIC X VALUE "N".
       01 WS-EMAIL-TRIM       PIC X(50).
       01 WS-EMAIL-LENGTH PIC 9(3) VALUE ZEROS.


       PROCEDURE DIVISION.
       MAIN-PROCEDURE.
           PERFORM INITIALIZE-CUSTOMER-ID.
           DISPLAY "R: Register  /  U: Update".
           ACCEPT WS-ACTION.

           EVALUATE WS-ACTION
               WHEN "R"
                   PERFORM REGISTER-CUSTOMER
               WHEN "U"
                   PERFORM UPDATE-CUSTOMER
               WHEN OTHER
                   DISPLAY "Invalid Option"
           END-EVALUATE.
           EXIT PROGRAM.

       INITIALIZE-CUSTOMER-ID.
           OPEN INPUT CUSTOMER-FILE.
           MOVE "N" TO WS-EOF.
           PERFORM UNTIL WS-EOF = "Y"
               READ CUSTOMER-FILE
                   AT END
                       MOVE "Y" TO WS-EOF
                   NOT AT END
                       MOVE CR-ID TO WS-CUSTOMER-ID
               END-READ
           END-PERFORM.
           CLOSE CUSTOMER-FILE.
           ADD 1 TO WS-CUSTOMER-ID.

       REGISTER-CUSTOMER.
           DISPLAY "Enter Name:".
           ACCEPT WS-NAME.
           DISPLAY "Enter Email:".
           ACCEPT WS-EMAIL.
           PERFORM VALIDATE-EMAIL.
           IF WS-VALID-DATA = "N"
               DISPLAY WS-ERROR-MESSAGE
               EXIT PARAGRAPH
           END-IF.
           DISPLAY "Enter Birthdate (YYYYMMDD):".
           ACCEPT WS-BIRTHDATE.
           
           PERFORM VALIDATE-BIRTHDATE.
           IF WS-VALID-DATA = "N"
               DISPLAY WS-ERROR-MESSAGE
               EXIT PARAGRAPH
           END-IF.

           OPEN EXTEND CUSTOMER-FILE.
           MOVE WS-CUSTOMER-ID TO CR-ID.
           MOVE WS-NAME TO CR-NAME.
           MOVE WS-EMAIL TO CR-EMAIL.
           MOVE WS-BIRTHDATE TO CR-BIRTHDATE.
           WRITE CUSTOMER-RECORD.
           CLOSE CUSTOMER-FILE.

           DISPLAY "Customer Registered Successfully!".
           DISPLAY "Customer ID: " WS-CUSTOMER-ID.
           ADD 1 TO WS-CUSTOMER-ID.

       UPDATE-CUSTOMER.
           DISPLAY "Enter Customer ID to update:".
           ACCEPT WS-CUSTOMER-ID.
           
           PERFORM VALIDATE-CUSTOMER-ID.
           IF WS-VALID-DATA = "N"
               DISPLAY WS-ERROR-MESSAGE
               EXIT PARAGRAPH
           END-IF.
           
           PERFORM READ-CUSTOMER-FILE.
           
           IF WS-FOUND = "Y"
               PERFORM UPDATE-CUSTOMER-INFO
           ELSE
               DISPLAY "Customer ID not found!"
           END-IF.

       UPDATE-CUSTOMER-INFO.
           DISPLAY "Enter new Name (current: " CR-NAME "):".
           ACCEPT WS-NAME.
           DISPLAY "Enter new Email (current: " CR-EMAIL "):".
           ACCEPT WS-EMAIL.
           PERFORM VALIDATE-EMAIL.
           IF WS-VALID-DATA = "N"
               DISPLAY WS-ERROR-MESSAGE
               EXIT PARAGRAPH
           END-IF.
           DISPLAY "Enter new Birthdate (current: " CR-BIRTHDATE "):".
           ACCEPT WS-BIRTHDATE.
           
           PERFORM VALIDATE-BIRTHDATE.
           IF WS-VALID-DATA = "N"
               DISPLAY WS-ERROR-MESSAGE
               EXIT PARAGRAPH
           END-IF.
           
           PERFORM UPDATE-CUSTOMER-FILE.
           DISPLAY "Customer updated successfully!".
           
       READ-CUSTOMER-FILE.
           MOVE "N" TO WS-FOUND.
           OPEN INPUT CUSTOMER-FILE.
           MOVE "N" TO WS-EOF.
           PERFORM UNTIL WS-EOF = "Y"
               READ CUSTOMER-FILE
                   AT END
                       MOVE "Y" TO WS-EOF
                   NOT AT END
                       IF CR-ID = WS-CUSTOMER-ID
                           MOVE "Y" TO WS-FOUND
                           MOVE "Y" TO WS-EOF
                       END-IF
               END-READ
           END-PERFORM.
           CLOSE CUSTOMER-FILE.
           
       UPDATE-CUSTOMER-FILE.
           PERFORM READ-ALL-CUSTOMERS.
           
           SET WS-IDX TO 1.
           PERFORM UNTIL WS-IDX > WS-CUSTOMER-COUNT
               IF WS-CR-ID(WS-IDX) = WS-CUSTOMER-ID
                   MOVE WS-NAME TO WS-CR-NAME(WS-IDX)
                   MOVE WS-EMAIL TO WS-CR-EMAIL(WS-IDX)
                   MOVE WS-BIRTHDATE TO WS-CR-BIRTHDATE(WS-IDX)
               END-IF
               SET WS-IDX UP BY 1
           END-PERFORM.
           
           PERFORM WRITE-UPDATED-CUSTOMERS.
           
       READ-ALL-CUSTOMERS.
           MOVE 0 TO WS-CUSTOMER-COUNT.
           SET WS-IDX TO 1.
           OPEN INPUT CUSTOMER-FILE.
           MOVE "N" TO WS-EOF.
           PERFORM UNTIL WS-EOF = "Y"
               READ CUSTOMER-FILE
                   AT END
                       MOVE "Y" TO WS-EOF
                   NOT AT END
                       MOVE CR-ID TO WS-CR-ID(WS-IDX)
                       MOVE CR-NAME TO WS-CR-NAME(WS-IDX)
                       MOVE CR-EMAIL TO WS-CR-EMAIL(WS-IDX)
                       MOVE CR-BIRTHDATE TO WS-CR-BIRTHDATE(WS-IDX)
                       
                       ADD 1 TO WS-CUSTOMER-COUNT
                       SET WS-IDX UP BY 1
               END-READ
           END-PERFORM.
           CLOSE CUSTOMER-FILE.
           
       WRITE-UPDATED-CUSTOMERS.
           OPEN OUTPUT CUSTOMER-FILE.
           SET WS-IDX TO 1.
           PERFORM WS-CUSTOMER-COUNT TIMES
               MOVE WS-CR-ID(WS-IDX) TO CR-ID
               MOVE WS-CR-NAME(WS-IDX) TO CR-NAME
               MOVE WS-CR-EMAIL(WS-IDX) TO CR-EMAIL
               MOVE WS-CR-BIRTHDATE(WS-IDX) TO CR-BIRTHDATE
               WRITE CUSTOMER-RECORD
               SET WS-IDX UP BY 1
           END-PERFORM.
           CLOSE CUSTOMER-FILE.
           
       VALIDATE-BIRTHDATE.
           MOVE "Y" TO WS-VALID-DATA.
           
           IF FUNCTION LENGTH(FUNCTION TRIM(WS-BIRTHDATE)) NOT = 8
               MOVE "N" TO WS-VALID-DATA
               MOVE "Birthdate must be 8 digits (YYYYMMDD)." 
                   TO WS-ERROR-MESSAGE
               EXIT PARAGRAPH
           END-IF.
           
           PERFORM CHECK-NUMERIC-DATE
           IF IS-NOT-NUMERIC
               MOVE "N" TO WS-VALID-DATA
               MOVE "Birthdate must contain only digits."
                   TO WS-ERROR-MESSAGE
               EXIT PARAGRAPH
           END-IF.
           
           ACCEPT WS-CURRENT-DATE FROM DATE YYYYMMDD.
           
           MOVE WS-BIRTHDATE(1:4) TO WS-BIRTH-YEAR.
           MOVE WS-BIRTHDATE(5:2) TO WS-BIRTH-MONTH.
           MOVE WS-BIRTHDATE(7:2) TO WS-BIRTH-DAY.
           
           IF WS-BIRTH-YEAR < 1900 OR WS-BIRTH-YEAR > WS-CURRENT-YEAR
               MOVE "N" TO WS-VALID-DATA
               MOVE "Invalid birth year."
                   TO WS-ERROR-MESSAGE
               EXIT PARAGRAPH
           END-IF.
           
           IF WS-BIRTH-MONTH < 1 OR WS-BIRTH-MONTH > 12
               MOVE "N" TO WS-VALID-DATA
               MOVE "Invalid birth month."
                   TO WS-ERROR-MESSAGE
               EXIT PARAGRAPH
           END-IF.
           
           PERFORM DETERMINE-DAYS-IN-MONTH
           IF WS-BIRTH-DAY < 1 OR WS-BIRTH-DAY > WS-DAYS-IN-MONTH
               MOVE "N" TO WS-VALID-DATA
               MOVE "Invalid day for the given month."
                   TO WS-ERROR-MESSAGE
               EXIT PARAGRAPH
           END-IF.
           
           IF WS-BIRTH-YEAR = WS-CURRENT-YEAR
               IF WS-BIRTH-MONTH > WS-CURRENT-MONTH OR 
                  (WS-BIRTH-MONTH = WS-CURRENT-MONTH AND 
                   WS-BIRTH-DAY > WS-CURRENT-DAY)
                   MOVE "N" TO WS-VALID-DATA
                   MOVE "Birthdate cannot be in the future."
                       TO WS-ERROR-MESSAGE
               END-IF
           END-IF.
           
       CHECK-NUMERIC-DATE.
           MOVE "Y" TO WS-NUMERIC-TEST.
           MOVE ZERO TO WS-TEMP-NUM
           INSPECT WS-BIRTHDATE
               TALLYING WS-TEMP-NUM FOR ALL "0" "1" "2" "3" "4"
                                         "5" "6" "7" "8" "9"
           IF WS-TEMP-NUM NOT = 8
               MOVE "N" TO WS-NUMERIC-TEST
           END-IF.

       DETERMINE-DAYS-IN-MONTH.
           EVALUATE WS-BIRTH-MONTH
               WHEN 1  MOVE 31 TO WS-DAYS-IN-MONTH
               WHEN 2  PERFORM CHECK-LEAP-YEAR
                       IF WS-LEAP-YEAR = "Y"
                           MOVE 29 TO WS-DAYS-IN-MONTH
                       ELSE
                           MOVE 28 TO WS-DAYS-IN-MONTH
                       END-IF
               WHEN 3  MOVE 31 TO WS-DAYS-IN-MONTH
               WHEN 4  MOVE 30 TO WS-DAYS-IN-MONTH
               WHEN 5  MOVE 31 TO WS-DAYS-IN-MONTH
               WHEN 6  MOVE 30 TO WS-DAYS-IN-MONTH
               WHEN 7  MOVE 31 TO WS-DAYS-IN-MONTH
               WHEN 8  MOVE 31 TO WS-DAYS-IN-MONTH
               WHEN 9  MOVE 30 TO WS-DAYS-IN-MONTH
               WHEN 10 MOVE 31 TO WS-DAYS-IN-MONTH
               WHEN 11 MOVE 30 TO WS-DAYS-IN-MONTH
               WHEN 12 MOVE 31 TO WS-DAYS-IN-MONTH
           END-EVALUATE.

       CHECK-LEAP-YEAR.
           MOVE "N" TO WS-LEAP-YEAR.
           IF FUNCTION MOD(WS-BIRTH-YEAR, 400) = 0
               MOVE "Y" TO WS-LEAP-YEAR
           ELSE
               IF FUNCTION MOD(WS-BIRTH-YEAR, 100) = 0
                   MOVE "N" TO WS-LEAP-YEAR
               ELSE
                   IF FUNCTION MOD(WS-BIRTH-YEAR, 4) = 0
                       MOVE "Y" TO WS-LEAP-YEAR
                   END-IF
               END-IF
           END-IF.

       VALIDATE-EMAIL.
           MOVE "Y" TO WS-VALID-DATA.

           IF WS-EMAIL = SPACES
               MOVE "N" TO WS-VALID-DATA
               MOVE "Email cannot be empty." TO WS-ERROR-MESSAGE
               EXIT PARAGRAPH
           END-IF.

           MOVE FUNCTION TRIM(WS-EMAIL) TO WS-EMAIL-TRIM.
           MOVE FUNCTION LOWER-CASE(WS-EMAIL-TRIM) TO WS-EMAIL-TRIM.
           MOVE FUNCTION TRIM(WS-EMAIL-TRIM) TO WS-EMAIL-TRIM.
           MOVE FUNCTION LENGTH(WS-EMAIL-TRIM) TO WS-EMAIL-LENGTH.

           MOVE 0 TO WS-AT-COUNT.
           INSPECT WS-EMAIL-TRIM(1:WS-EMAIL-LENGTH)
               TALLYING WS-AT-COUNT FOR ALL "@".

           IF WS-AT-COUNT NOT = 1
               MOVE "N" TO WS-VALID-DATA
               MOVE "Email must contain exactly one @ symbol." 
                   TO WS-ERROR-MESSAGE
               EXIT PARAGRAPH
           END-IF.

           MOVE 0 TO WS-DOT-COUNT.
           INSPECT WS-EMAIL-TRIM(1:WS-EMAIL-LENGTH)
               TALLYING WS-DOT-COUNT FOR ALL ".".

           IF WS-DOT-COUNT < 1
               MOVE "N" TO WS-VALID-DATA
               MOVE "Email must contain at least one dot."
                   TO WS-ERROR-MESSAGE
               EXIT PARAGRAPH
           END-IF.

           MOVE WS-EMAIL-TRIM TO WS-EMAIL.

       VALIDATE-CUSTOMER-ID.
           MOVE "Y" TO WS-VALID-DATA.
           
           IF WS-CUSTOMER-ID < 1 OR WS-CUSTOMER-ID > 99999
               MOVE "N" TO WS-VALID-DATA
               MOVE "Invalid customer ID format." TO WS-ERROR-MESSAGE
           END-IF.
