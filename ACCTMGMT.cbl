       IDENTIFICATION DIVISION.
       PROGRAM-ID. ACCTMGMT.

       ENVIRONMENT DIVISION.
       INPUT-OUTPUT SECTION.
       FILE-CONTROL.
           SELECT ACCOUNT-FILE ASSIGN TO "ACCOUNT.dat"
           ORGANIZATION IS LINE SEQUENTIAL.
           SELECT TRANSACTION-FILE ASSIGN TO "TRANSACT.dat"
           ORGANIZATION IS LINE SEQUENTIAL.

       DATA DIVISION.
       FILE SECTION.
       FD ACCOUNT-FILE.
       01 ACCOUNT-RECORD.
           05 AC-ID              PIC 9(6).
           05 AC-CUST-ID         PIC 9(5).
           05 AC-TYPE            PIC X(1).
           05 AC-BALANCE         PIC 9(6)V99.

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

       WORKING-STORAGE SECTION.
       01 WS-ACCOUNT-ID      PIC 9(6) VALUE 100001.
       01 WS-CUST-ID         PIC 9(5).
       01 WS-ACCTYPE         PIC X(1).
           88 SAVINGS        VALUE "S".
           88 CHECKING       VALUE "C".
       01 WS-BALANCE         PIC 9(6)V99 VALUE 0.
       01 WS-AMOUNT          PIC 9(6)V99.
       01 WS-ACTION          PIC X.
       01 WS-EOF             PIC X VALUE "N".
       01 WS-FOUND           PIC X VALUE "N".
       01 WS-TRANSACTION-ID  PIC 9(8) VALUE 10000001.
       01 WS-CURRENT-DATE-TIME      PIC X(16).
       01 WS-DATE                   PIC 9(8).
       01 WS-TIME                   PIC 9(6).
       01 WS-VALID-DATA       PIC X VALUE "Y".
       01 WS-MIN-AMOUNT       PIC 9(1)V99 VALUE 0.01.
       01 WS-MAX-AMOUNT       PIC 9(6)V99 VALUE 999999.99.
       01 WS-ERROR-MESSAGE    PIC X(50).
       
       01 WS-ACCOUNT-TABLE.
          05 WS-ACCOUNT-ENTRY OCCURS 100 TIMES INDEXED BY WS-IDX.
             10 WS-AC-ID              PIC 9(6).
             10 WS-AC-CUST-ID         PIC 9(5).
             10 WS-AC-TYPE            PIC X(1).
             10 WS-AC-BALANCE         PIC 9(6)V99.
       01 WS-ACCOUNT-COUNT       PIC 9(3) VALUE 0.

       PROCEDURE DIVISION.
       MAIN-PROCEDURE.
           PERFORM INITIALIZE-IDS.
           DISPLAY "A: Add Account  /  D: Deposit  /  W: Withdraw".
           ACCEPT WS-ACTION.

           EVALUATE WS-ACTION
               WHEN "A"
                   PERFORM ADD-ACCOUNT
               WHEN "D"
                   PERFORM DEPOSIT
               WHEN "W"
                   PERFORM WITHDRAW
               WHEN OTHER
                   DISPLAY "Invalid Option"
           END-EVALUATE.
           EXIT PROGRAM.

       INITIALIZE-IDS.
           PERFORM INITIALIZE-ACCOUNT-ID.
           PERFORM INITIALIZE-TRANSACTION-ID.
           ACCEPT WS-CURRENT-DATE-TIME FROM DATE YYYYMMDD.
           MOVE WS-CURRENT-DATE-TIME(1:8) TO WS-DATE.
           MOVE WS-CURRENT-DATE-TIME(9:6) TO WS-TIME.

       INITIALIZE-ACCOUNT-ID.
           OPEN INPUT ACCOUNT-FILE.
           MOVE "N" TO WS-EOF.
           PERFORM UNTIL WS-EOF = "Y"
               READ ACCOUNT-FILE
                   AT END
                       MOVE "Y" TO WS-EOF
                   NOT AT END
                       MOVE AC-ID TO WS-ACCOUNT-ID
               END-READ
           END-PERFORM.
           CLOSE ACCOUNT-FILE.
           ADD 1 TO WS-ACCOUNT-ID.

       INITIALIZE-TRANSACTION-ID.
           OPEN INPUT TRANSACTION-FILE.
           MOVE "N" TO WS-EOF.
           PERFORM UNTIL WS-EOF = "Y"
               READ TRANSACTION-FILE
                   AT END
                       MOVE "Y" TO WS-EOF
                   NOT AT END
                       MOVE TR-ID TO WS-TRANSACTION-ID
               END-READ
           END-PERFORM.
           CLOSE TRANSACTION-FILE.
           ADD 1 TO WS-TRANSACTION-ID.

       ADD-ACCOUNT.
           DISPLAY "Enter Customer ID:".
           ACCEPT WS-CUST-ID.
           
           PERFORM VALIDATE-CUSTOMER-ID.
           IF WS-VALID-DATA = "N"
               DISPLAY WS-ERROR-MESSAGE
               EXIT PARAGRAPH
           END-IF.
           
           DISPLAY "Account Type (S/C):".
           ACCEPT WS-ACCTYPE.

           IF SAVINGS OR CHECKING
               OPEN EXTEND ACCOUNT-FILE
               MOVE WS-ACCOUNT-ID TO AC-ID
               MOVE WS-CUST-ID TO AC-CUST-ID
               MOVE WS-ACCTYPE TO AC-TYPE
               MOVE 0 TO AC-BALANCE
               WRITE ACCOUNT-RECORD
               CLOSE ACCOUNT-FILE
               
               DISPLAY "Account created. ID: " WS-ACCOUNT-ID
               ADD 1 TO WS-ACCOUNT-ID
           ELSE
               DISPLAY "Invalid account type."
           END-IF.

       DEPOSIT.
           DISPLAY "Enter Account ID:".
           ACCEPT WS-ACCOUNT-ID.
           
           PERFORM VALIDATE-ACCOUNT-ID.
           IF WS-VALID-DATA = "N"
               DISPLAY WS-ERROR-MESSAGE
               EXIT PARAGRAPH
           END-IF.
           
           PERFORM READ-ACCOUNT.
           
           IF WS-FOUND = "Y"
               PERFORM PROCESS-DEPOSIT
           ELSE
               DISPLAY "Account not found!"
           END-IF.
           
       PROCESS-DEPOSIT.
           DISPLAY "Enter Amount to Deposit:".
           ACCEPT WS-AMOUNT.
           
           PERFORM VALIDATE-AMOUNT.
           IF WS-VALID-DATA = "Y"
               ADD WS-AMOUNT TO WS-BALANCE
               PERFORM UPDATE-ACCOUNT-BALANCE
               PERFORM RECORD-TRANSACTION
               DISPLAY "Deposit Successful. Balance: " WS-BALANCE
           ELSE
               DISPLAY WS-ERROR-MESSAGE
           END-IF.

       WITHDRAW.
           DISPLAY "Enter Account ID:".
           ACCEPT WS-ACCOUNT-ID.
           
           PERFORM VALIDATE-ACCOUNT-ID.
           IF WS-VALID-DATA = "N"
               DISPLAY WS-ERROR-MESSAGE
               EXIT PARAGRAPH
           END-IF.
           
           PERFORM READ-ACCOUNT.
           
           IF WS-FOUND = "Y"
               PERFORM PROCESS-WITHDRAW
           ELSE
               DISPLAY "Account not found!"
           END-IF.
           
       PROCESS-WITHDRAW.
           DISPLAY "Enter Amount to Withdraw:".
           ACCEPT WS-AMOUNT.
           
           PERFORM VALIDATE-AMOUNT.
           IF WS-VALID-DATA = "Y"
               IF WS-AMOUNT > WS-BALANCE
                   DISPLAY "Insufficient funds."
               ELSE
                   PERFORM COMPLETE-WITHDRAW
               END-IF
           ELSE
               DISPLAY WS-ERROR-MESSAGE
           END-IF.
           
       COMPLETE-WITHDRAW.
           SUBTRACT WS-AMOUNT FROM WS-BALANCE.
           PERFORM UPDATE-ACCOUNT-BALANCE.
           PERFORM RECORD-TRANSACTION.
           DISPLAY "Withdrawn successfully. Balance: " WS-BALANCE.
           
       READ-ACCOUNT.
           MOVE "N" TO WS-FOUND.
           OPEN INPUT ACCOUNT-FILE.
           MOVE "N" TO WS-EOF.
           PERFORM UNTIL WS-EOF = "Y"
               READ ACCOUNT-FILE
                   AT END
                       MOVE "Y" TO WS-EOF
                   NOT AT END
                       IF AC-ID = WS-ACCOUNT-ID
                           MOVE "Y" TO WS-FOUND
                           MOVE "Y" TO WS-EOF
                           MOVE AC-BALANCE TO WS-BALANCE
                       END-IF
               END-READ
           END-PERFORM.
           CLOSE ACCOUNT-FILE.
           
       UPDATE-ACCOUNT-BALANCE.
           PERFORM READ-ALL-ACCOUNTS.
           PERFORM WRITE-UPDATED-ACCOUNTS.
           
       READ-ALL-ACCOUNTS.
           MOVE 0 TO WS-ACCOUNT-COUNT.
           SET WS-IDX TO 1.
           OPEN INPUT ACCOUNT-FILE.
           MOVE "N" TO WS-EOF.
           PERFORM UNTIL WS-EOF = "Y"
               READ ACCOUNT-FILE
                   AT END
                       MOVE "Y" TO WS-EOF
                   NOT AT END
                       MOVE AC-ID TO WS-AC-ID(WS-IDX)
                       MOVE AC-CUST-ID TO WS-AC-CUST-ID(WS-IDX)
                       MOVE AC-TYPE TO WS-AC-TYPE(WS-IDX)
                       MOVE AC-BALANCE TO WS-AC-BALANCE(WS-IDX)
                       
                       IF AC-ID = WS-ACCOUNT-ID
                           MOVE WS-BALANCE TO WS-AC-BALANCE(WS-IDX)
                       END-IF
                       
                       ADD 1 TO WS-ACCOUNT-COUNT
                       SET WS-IDX UP BY 1
               END-READ
           END-PERFORM.
           CLOSE ACCOUNT-FILE.
           
       WRITE-UPDATED-ACCOUNTS.
           OPEN OUTPUT ACCOUNT-FILE.
           SET WS-IDX TO 1.
           PERFORM WS-ACCOUNT-COUNT TIMES
               MOVE WS-AC-ID(WS-IDX) TO AC-ID
               MOVE WS-AC-CUST-ID(WS-IDX) TO AC-CUST-ID
               MOVE WS-AC-TYPE(WS-IDX) TO AC-TYPE
               MOVE WS-AC-BALANCE(WS-IDX) TO AC-BALANCE
               WRITE ACCOUNT-RECORD
               SET WS-IDX UP BY 1
           END-PERFORM.
           CLOSE ACCOUNT-FILE.
           
       RECORD-TRANSACTION.
           OPEN EXTEND TRANSACTION-FILE.
           MOVE WS-TRANSACTION-ID TO TR-ID.
           MOVE WS-ACCOUNT-ID TO TR-ACCOUNT-ID.
           
           IF WS-ACTION = "D"
               MOVE "D" TO TR-TYPE
           ELSE
               MOVE "W" TO TR-TYPE
           END-IF.
           
           MOVE WS-AMOUNT TO TR-AMOUNT.
           MOVE WS-DATE TO TR-DATE.
           MOVE WS-TIME TO TR-TIME.
           WRITE TRANSACTION-RECORD.
           CLOSE TRANSACTION-FILE.
           ADD 1 TO WS-TRANSACTION-ID.
           
       VALIDATE-AMOUNT.
           MOVE "Y" TO WS-VALID-DATA.
           
           IF WS-AMOUNT <= 0.01
               MOVE "N" TO WS-VALID-DATA
               STRING "Amount must be greater than zero." 
                   DELIMITED BY SIZE INTO WS-ERROR-MESSAGE
           END-IF.
           
           IF WS-AMOUNT > 999999.99
               MOVE "N" TO WS-VALID-DATA
               STRING "Amount exceeds maximum transaction limit." 
                   DELIMITED BY SIZE INTO WS-ERROR-MESSAGE
           END-IF.

       VALIDATE-ACCOUNT-ID.
           MOVE "Y" TO WS-VALID-DATA.
           
           IF WS-ACCOUNT-ID < 100001 OR WS-ACCOUNT-ID > 999999
               MOVE "N" TO WS-VALID-DATA
               STRING "Invalid account ID format." 
                   DELIMITED BY SIZE INTO WS-ERROR-MESSAGE
           END-IF.

       VALIDATE-CUSTOMER-ID.
           MOVE "Y" TO WS-VALID-DATA.
           
           IF WS-CUST-ID < 1 OR WS-CUST-ID > 99999
               MOVE "N" TO WS-VALID-DATA
               STRING "Invalid customer ID format." 
                   DELIMITED BY SIZE INTO WS-ERROR-MESSAGE
           END-IF.
