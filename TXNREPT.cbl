       IDENTIFICATION DIVISION.
       PROGRAM-ID. TXNREPT.

       ENVIRONMENT DIVISION.
       INPUT-OUTPUT SECTION.
       FILE-CONTROL.
           SELECT TRANSACTION-FILE ASSIGN TO "TRANSACT.dat"
           ORGANIZATION IS LINE SEQUENTIAL.
           SELECT ACCOUNT-FILE ASSIGN TO "ACCOUNT.dat"
           ORGANIZATION IS LINE SEQUENTIAL.
           SELECT CUSTOMER-FILE ASSIGN TO "CUSTOMER.dat"
           ORGANIZATION IS LINE SEQUENTIAL.

       DATA DIVISION.
       FILE SECTION.
       FD TRANSACTION-FILE.
       01 TRANSACTION-RECORD.
           05 TR-ID              PIC 9(8).
           05 TR-ACCOUNT-ID      PIC 9(6).
           05 TR-TYPE            PIC X(1).
               88 TR-DEPOSIT     VALUE "D".
               88 TR-WITHDRAW    VALUE "W".
           05 TR-AMOUNT          PIC 9(6)V99.
           05 TR-DATE            PIC 9(8).
           05 TR-TIME            PIC 9(6).

       FD ACCOUNT-FILE.
       01 ACCOUNT-RECORD.
           05 AC-ID              PIC 9(6).
           05 AC-CUST-ID         PIC 9(5).
           05 AC-TYPE            PIC X(1).
           05 AC-BALANCE         PIC 9(6)V99.

       FD CUSTOMER-FILE.
       01 CUSTOMER-RECORD.
           05 CR-ID              PIC 9(5).
           05 CR-NAME            PIC X(30).
           05 CR-EMAIL           PIC X(50).
           05 CR-BIRTHDATE       PIC 9(8).

       WORKING-STORAGE SECTION.
       01 WS-CUST-ID         PIC 9(5).
       01 WS-START-DATE      PIC 9(8).
       01 WS-END-DATE        PIC 9(8).
       01 WS-REPORT-LINE     PIC X(80).
       01 WS-EOF             PIC X VALUE "N".
       01 WS-FOUND           PIC X VALUE "N".
       01 WS-CUSTOMER-NAME   PIC X(30).
       01 WS-ACCOUNT-TABLE.
          05 WS-ACCOUNT-ENTRY OCCURS 50 TIMES INDEXED BY WS-AC-IDX.
             10 WS-AC-ID              PIC 9(6).
       01 WS-ACCOUNT-COUNT    PIC 9(2) VALUE 0.
       01 WS-TRANSACTION-COUNT PIC 9(3) VALUE 0.
       01 WS-FORMATTED-DATE.
           05 WS-YEAR         PIC 9(4).
           05 FILLER          PIC X VALUE "/".
           05 WS-MONTH        PIC 9(2).
           05 FILLER          PIC X VALUE "/".
           05 WS-DAY          PIC 9(2).
           
       01 WS-FORMATTED-AMOUNT PIC Z,ZZZ,ZZ9.99.
       01 WS-VALID-DATA       PIC X VALUE "Y".
       01 WS-ERROR-MESSAGE    PIC X(50).
       01 WS-CURRENT-DATE     PIC 9(8).
       01 WS-DATE-COMPONENTS.
          05 WS-DATE-YEAR    PIC 9(4).
          05 WS-DATE-MONTH   PIC 9(2).
          05 WS-DATE-DAY     PIC 9(2).
       01 WS-DAYS-IN-MONTH   PIC 9(2).
       01 WS-LEAP-YEAR       PIC X VALUE "N".
       01 WS-TEMP-NUM        PIC 9(8).
       01 WS-NUMERIC-TEST    PIC X.
          88 IS-NUMERIC      VALUE "Y".
          88 IS-NOT-NUMERIC  VALUE "N".

       PROCEDURE DIVISION.
       MAIN-PROCEDURE.
           DISPLAY "Enter Customer ID:".
           ACCEPT WS-CUST-ID.
           
           PERFORM VALIDATE-CUSTOMER-ID.
           IF WS-VALID-DATA = "N"
               DISPLAY WS-ERROR-MESSAGE
               EXIT PROGRAM
           END-IF.
           
           DISPLAY "Enter Start Date (YYYYMMDD):".
           ACCEPT WS-START-DATE.
           PERFORM VALIDATE-START-DATE.
           IF WS-VALID-DATA = "N"
               DISPLAY WS-ERROR-MESSAGE
               EXIT PROGRAM
           END-IF.
           
           DISPLAY "Enter End Date (YYYYMMDD):".
           ACCEPT WS-END-DATE.
           PERFORM VALIDATE-END-DATE.
           IF WS-VALID-DATA = "N"
               DISPLAY WS-ERROR-MESSAGE
               EXIT PROGRAM
           END-IF.
           
           PERFORM VALIDATE-DATE-RANGE.
           IF WS-VALID-DATA = "N"
               DISPLAY WS-ERROR-MESSAGE
               EXIT PROGRAM
           END-IF.

           PERFORM GET-CUSTOMER-NAME.
           
           IF WS-FOUND = "Y"
               PERFORM PROCESS-REPORT
           ELSE
               DISPLAY "Customer ID not found!"
           END-IF.
           
       PROCESS-REPORT.
           DISPLAY "Generating transaction report for " WS-CUSTOMER-NAME.
           PERFORM GET-CUSTOMER-ACCOUNTS.
           PERFORM GENERATE-REPORT.
           
           EXIT PROGRAM.

       GET-CUSTOMER-NAME.
           MOVE "N" TO WS-FOUND.
           OPEN INPUT CUSTOMER-FILE.
           MOVE "N" TO WS-EOF.
           PERFORM UNTIL WS-EOF = "Y"
               READ CUSTOMER-FILE
                   AT END
                       MOVE "Y" TO WS-EOF
                   NOT AT END
                       IF CR-ID = WS-CUST-ID
                           MOVE "Y" TO WS-FOUND
                           MOVE CR-NAME TO WS-CUSTOMER-NAME
                           MOVE "Y" TO WS-EOF
                       END-IF
               END-READ
           END-PERFORM.
           CLOSE CUSTOMER-FILE.

       GET-CUSTOMER-ACCOUNTS.
           OPEN INPUT ACCOUNT-FILE.
           MOVE "N" TO WS-EOF.
           MOVE 0 TO WS-ACCOUNT-COUNT.
           SET WS-AC-IDX TO 1.
           PERFORM UNTIL WS-EOF = "Y"
               READ ACCOUNT-FILE
                   AT END
                       MOVE "Y" TO WS-EOF
                   NOT AT END
                       IF AC-CUST-ID = WS-CUST-ID
                           MOVE AC-ID TO WS-AC-ID(WS-AC-IDX)
                           ADD 1 TO WS-ACCOUNT-COUNT
                           SET WS-AC-IDX UP BY 1
                       END-IF
               END-READ
           END-PERFORM.
           CLOSE ACCOUNT-FILE.

       GENERATE-REPORT.
           DISPLAY "===================================".
           DISPLAY "TRANSACTION REPORT".
           DISPLAY "Customer: " WS-CUSTOMER-NAME.
           DISPLAY "Period: " WS-START-DATE " to " WS-END-DATE.
           DISPLAY "===================================".
           
           OPEN INPUT TRANSACTION-FILE.
           MOVE "N" TO WS-EOF.
           MOVE 0 TO WS-TRANSACTION-COUNT.
           
           PERFORM UNTIL WS-EOF = "Y"
               READ TRANSACTION-FILE
                   AT END
                       MOVE "Y" TO WS-EOF
                   NOT AT END
                       PERFORM CHECK-AND-DISPLAY-TRANSACTION
               END-READ
           END-PERFORM.
           
           CLOSE TRANSACTION-FILE.
           
           PERFORM DISPLAY-SUMMARY.
           
       DISPLAY-SUMMARY.
           IF WS-TRANSACTION-COUNT = 0
               PERFORM DISPLAY-NO-TRANSACTIONS
           ELSE
               PERFORM DISPLAY-TRANSACTION-COUNT
           END-IF.
           
       DISPLAY-NO-TRANSACTIONS.
           DISPLAY "No transactions found for this period.".
           
       DISPLAY-TRANSACTION-COUNT.
           DISPLAY "===================================".
           DISPLAY WS-TRANSACTION-COUNT " transaction(s) found.".

       CHECK-AND-DISPLAY-TRANSACTION.
           IF TR-DATE >= WS-START-DATE AND TR-DATE <= WS-END-DATE
               PERFORM CHECK-ACCOUNT-BELONGS-TO-CUSTOMER
           END-IF.

       CHECK-ACCOUNT-BELONGS-TO-CUSTOMER.
           PERFORM CHECK-ACCOUNT-ID.
           
       CHECK-ACCOUNT-ID.
           MOVE "N" TO WS-FOUND.
           SET WS-AC-IDX TO 1.
           PERFORM UNTIL WS-AC-IDX > WS-ACCOUNT-COUNT OR WS-FOUND = "Y"
               IF WS-AC-ID(WS-AC-IDX) = TR-ACCOUNT-ID
                   MOVE "Y" TO WS-FOUND
               END-IF
               SET WS-AC-IDX UP BY 1
           END-PERFORM.
           
           IF WS-FOUND = "Y"
               PERFORM FORMAT-AND-DISPLAY-TRANSACTION
           END-IF.

       FORMAT-AND-DISPLAY-TRANSACTION.
           MOVE TR-DATE(1:4) TO WS-YEAR.
           MOVE TR-DATE(5:2) TO WS-MONTH.
           MOVE TR-DATE(7:2) TO WS-DAY.
           
           MOVE SPACES TO WS-REPORT-LINE.
           
           IF TR-TYPE = "D"
               PERFORM FORMAT-DEPOSIT
           ELSE
               PERFORM FORMAT-WITHDRAW
           END-IF.
           
           DISPLAY WS-REPORT-LINE.
           ADD 1 TO WS-TRANSACTION-COUNT.
           
       FORMAT-DEPOSIT.
           MOVE TR-AMOUNT TO WS-FORMATTED-AMOUNT.
           STRING WS-FORMATTED-DATE DELIMITED BY SIZE
                  " Account: " DELIMITED BY SIZE
                  TR-ACCOUNT-ID DELIMITED BY SIZE
                  " Deposit $" DELIMITED BY SIZE
                  WS-FORMATTED-AMOUNT DELIMITED BY SIZE
                  INTO WS-REPORT-LINE.
                  
       FORMAT-WITHDRAW.
           MOVE TR-AMOUNT TO WS-FORMATTED-AMOUNT.
           STRING WS-FORMATTED-DATE DELIMITED BY SIZE
                  " Account: " DELIMITED BY SIZE
                  TR-ACCOUNT-ID DELIMITED BY SIZE
                  " Withdraw $" DELIMITED BY SIZE
                  WS-FORMATTED-AMOUNT DELIMITED BY SIZE
                  INTO WS-REPORT-LINE.
                  
       VALIDATE-START-DATE.
           MOVE "Y" TO WS-VALID-DATA.
           
      *    Check if input is 8 digits long
           IF FUNCTION LENGTH(FUNCTION TRIM(WS-START-DATE)) NOT = 8
               MOVE "N" TO WS-VALID-DATA
               STRING "Start date must be 8 digits (YYYYMMDD)."
                   DELIMITED BY SIZE INTO WS-ERROR-MESSAGE
               EXIT PARAGRAPH
           END-IF.
           
      *    Check if input is numeric
           MOVE "Y" TO WS-NUMERIC-TEST.
           MOVE ZERO TO WS-TEMP-NUM
           INSPECT WS-START-DATE
               TALLYING WS-TEMP-NUM FOR ALL "0" "1" "2" "3" "4"
                                         "5" "6" "7" "8" "9"
           IF WS-TEMP-NUM NOT = 8
               MOVE "N" TO WS-NUMERIC-TEST
           END-IF.
           
           IF IS-NOT-NUMERIC
               MOVE "N" TO WS-VALID-DATA
               STRING "Start date must contain only digits."
                   DELIMITED BY SIZE INTO WS-ERROR-MESSAGE
               EXIT PARAGRAPH
           END-IF.
           
           MOVE WS-START-DATE(1:4) TO WS-DATE-YEAR.
           MOVE WS-START-DATE(5:2) TO WS-DATE-MONTH.
           MOVE WS-START-DATE(7:2) TO WS-DATE-DAY.
           
           PERFORM VALIDATE-DATE-COMPONENTS.

       VALIDATE-END-DATE.
           MOVE "Y" TO WS-VALID-DATA.
           
      *    Check if input is 8 digits long
           IF FUNCTION LENGTH(FUNCTION TRIM(WS-END-DATE)) NOT = 8
               MOVE "N" TO WS-VALID-DATA
               STRING "End date must be 8 digits (YYYYMMDD)."
                   DELIMITED BY SIZE INTO WS-ERROR-MESSAGE
               EXIT PARAGRAPH
           END-IF.
           
      *    Check if input is numeric
           MOVE "Y" TO WS-NUMERIC-TEST.
           MOVE ZERO TO WS-TEMP-NUM
           INSPECT WS-END-DATE
               TALLYING WS-TEMP-NUM FOR ALL "0" "1" "2" "3" "4"
                                         "5" "6" "7" "8" "9"
           IF WS-TEMP-NUM NOT = 8
               MOVE "N" TO WS-NUMERIC-TEST
           END-IF.
           
           IF IS-NOT-NUMERIC
               MOVE "N" TO WS-VALID-DATA
               STRING "End date must contain only digits."
                   DELIMITED BY SIZE INTO WS-ERROR-MESSAGE
               EXIT PARAGRAPH
           END-IF.
           
           MOVE WS-END-DATE(1:4) TO WS-DATE-YEAR.
           MOVE WS-END-DATE(5:2) TO WS-DATE-MONTH.
           MOVE WS-END-DATE(7:2) TO WS-DATE-DAY.
           
           PERFORM VALIDATE-DATE-COMPONENTS.

       VALIDATE-DATE-COMPONENTS.
      *    Check year
           IF WS-DATE-YEAR < 1900 OR WS-DATE-YEAR > 9999
               MOVE "N" TO WS-VALID-DATA
               STRING "Invalid year format."
                   DELIMITED BY SIZE INTO WS-ERROR-MESSAGE
               EXIT PARAGRAPH
           END-IF.
           
      *    Check month
           IF WS-DATE-MONTH < 1 OR WS-DATE-MONTH > 12
               MOVE "N" TO WS-VALID-DATA
               STRING "Invalid month format."
                   DELIMITED BY SIZE INTO WS-ERROR-MESSAGE
               EXIT PARAGRAPH
           END-IF.
           
      *    Check day based on month
           PERFORM DETERMINE-DAYS-IN-MONTH
           IF WS-DATE-DAY < 1 OR WS-DATE-DAY > WS-DAYS-IN-MONTH
               MOVE "N" TO WS-VALID-DATA
               STRING "Invalid day for the given month."
                   DELIMITED BY SIZE INTO WS-ERROR-MESSAGE
               EXIT PARAGRAPH
           END-IF.

       DETERMINE-DAYS-IN-MONTH.
           EVALUATE WS-DATE-MONTH
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
           IF FUNCTION MOD(WS-DATE-YEAR, 400) = 0
               MOVE "Y" TO WS-LEAP-YEAR
           ELSE
               IF FUNCTION MOD(WS-DATE-YEAR, 100) = 0
                   MOVE "N" TO WS-LEAP-YEAR
               ELSE
                   IF FUNCTION MOD(WS-DATE-YEAR, 4) = 0
                       MOVE "Y" TO WS-LEAP-YEAR
                   END-IF
               END-IF
           END-IF.

       VALIDATE-DATE-RANGE.
           MOVE "Y" TO WS-VALID-DATA.
           
           ACCEPT WS-CURRENT-DATE FROM DATE YYYYMMDD.
           
           IF WS-START-DATE > WS-END-DATE
               MOVE "N" TO WS-VALID-DATA
               STRING "Start date must be before or equal to end date."
                   DELIMITED BY SIZE INTO WS-ERROR-MESSAGE
               EXIT PARAGRAPH
           END-IF.
           
           IF WS-START-DATE > WS-CURRENT-DATE 
               OR WS-END-DATE > WS-CURRENT-DATE
               MOVE "N" TO WS-VALID-DATA
               STRING "Dates cannot be in the future."
                   DELIMITED BY SIZE INTO WS-ERROR-MESSAGE
           END-IF.

       VALIDATE-CUSTOMER-ID.
           MOVE "Y" TO WS-VALID-DATA.
           
           IF WS-CUST-ID < 1 OR WS-CUST-ID > 99999
               MOVE "N" TO WS-VALID-DATA
               STRING "Invalid customer ID format."
                   DELIMITED BY SIZE INTO WS-ERROR-MESSAGE
           END-IF.
