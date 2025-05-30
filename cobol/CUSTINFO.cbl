       IDENTIFICATION DIVISION.
       PROGRAM-ID. CUSTINFO.

       ENVIRONMENT DIVISION.
       INPUT-OUTPUT SECTION.
       FILE-CONTROL.
           SELECT CUSTOMER-FILE ASSIGN TO "CUSTOMER.dat"
           ORGANIZATION IS LINE SEQUENTIAL.
           SELECT ACCOUNT-FILE ASSIGN TO "ACCOUNT.dat"
           ORGANIZATION IS LINE SEQUENTIAL.

       DATA DIVISION.
       FILE SECTION.
       FD CUSTOMER-FILE.
       01 CUSTOMER-RECORD.
           05 CR-ID              PIC 9(5).
           05 CR-NAME            PIC X(30).
           05 CR-EMAIL           PIC X(50).
           05 CR-BIRTHDATE       PIC 9(8).

       FD ACCOUNT-FILE.
       01 ACCOUNT-RECORD.
           05 AC-ID              PIC 9(6).
           05 AC-CUST-ID         PIC 9(5).
           05 AC-TYPE            PIC X(1).
           05 AC-BALANCE         PIC 9(6)V99.

       WORKING-STORAGE SECTION.
       01 WS-CUST-ID         PIC 9(5).
       01 WS-EOF             PIC X VALUE "N".
       01 WS-FOUND           PIC X VALUE "N".
       01 WS-ACCOUNT-COUNT   PIC 9(3) VALUE 0.
       01 WS-FORMATTED-DATE.
           05 WS-YEAR         PIC 9(4).
           05 FILLER          PIC X VALUE "/".
           05 WS-MONTH        PIC 9(2).
           05 FILLER          PIC X VALUE "/".
           05 WS-DAY          PIC 9(2).
       01 WS-DUMMY           PIC X.

       PROCEDURE DIVISION.
       MAIN-PROCEDURE.
           DISPLAY "Enter Customer ID:".
           ACCEPT WS-CUST-ID.
           
           PERFORM GET-CUSTOMER-INFO.
           
           IF WS-FOUND = "Y"
               PERFORM DISPLAY-CUSTOMER-INFO
               PERFORM GET-CUSTOMER-ACCOUNTS
           ELSE
               DISPLAY "Customer ID not found!"
           END-IF.
           
           EXIT PROGRAM.
           
       GET-CUSTOMER-INFO.
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
                           MOVE "Y" TO WS-EOF
                       END-IF
               END-READ
           END-PERFORM.
           CLOSE CUSTOMER-FILE.
           
       DISPLAY-CUSTOMER-INFO.
           DISPLAY "===================================".
           DISPLAY "CUSTOMER INFORMATION".
           DISPLAY "===================================".
           DISPLAY "Customer ID: " CR-ID.
           DISPLAY "Name: " CR-NAME.
           DISPLAY "Email: " CR-EMAIL.
           
           MOVE CR-BIRTHDATE(1:4) TO WS-YEAR.
           MOVE CR-BIRTHDATE(5:2) TO WS-MONTH.
           MOVE CR-BIRTHDATE(7:2) TO WS-DAY.
           
           DISPLAY "Birthdate: " WS-FORMATTED-DATE.
           DISPLAY "===================================".
           
       GET-CUSTOMER-ACCOUNTS.
           MOVE 0 TO WS-ACCOUNT-COUNT.
           OPEN INPUT ACCOUNT-FILE.
           MOVE "N" TO WS-EOF.
           
           DISPLAY "ACCOUNTS:".
           DISPLAY "-----------------------------------".
           
           PERFORM UNTIL WS-EOF = "Y"
               READ ACCOUNT-FILE
                   AT END
                       MOVE "Y" TO WS-EOF
                   NOT AT END
                       IF AC-CUST-ID = WS-CUST-ID
                           PERFORM DISPLAY-ACCOUNT-INFO
                           ADD 1 TO WS-ACCOUNT-COUNT
                       END-IF
               END-READ
           END-PERFORM.
           
           CLOSE ACCOUNT-FILE.
           
           IF WS-ACCOUNT-COUNT = 0
               DISPLAY "No accounts found for this customer."
           ELSE
               DISPLAY "-----------------------------------"
               DISPLAY "Total accounts: " WS-ACCOUNT-COUNT
           END-IF.
           
       DISPLAY-ACCOUNT-INFO.
           DISPLAY "Account ID: " AC-ID.
           IF AC-TYPE = "S"
               DISPLAY "Type: Savings"
           ELSE
               DISPLAY "Type: Checking"
           END-IF.
           DISPLAY "Balance: " AC-BALANCE.
           DISPLAY "-----------------------------------".
           
       PRESS-ENTER.
           DISPLAY "Press ENTER to return to the menu...".
           ACCEPT WS-DUMMY.
