       IDENTIFICATION DIVISION.
       PROGRAM-ID. MAINMENU.

       DATA DIVISION.
       WORKING-STORAGE SECTION.
       01 WS-CHOICE        PIC 9 VALUE 0.
       01 WS-CLEAR         PIC X(10) VALUE X'1B5B324A1B5B48'.
       01 WS-DUMMY         PIC X.

       PROCEDURE DIVISION.
       MAIN-PARAGRAPH.
           PERFORM UNTIL WS-CHOICE = 9
               PERFORM DISPLAY-MENU
               PERFORM HANDLE-CHOICE
           END-PERFORM
           DISPLAY "Goodbye!"
           STOP RUN.

       DISPLAY-MENU.
           DISPLAY WS-CLEAR NO ADVANCING
           DISPLAY "===================================".
           DISPLAY "     COBOL BANKING SYSTEM MENU     ".
           DISPLAY "===================================".
           DISPLAY "1 - Register/Update Customer".
           DISPLAY "2 - Manage Account (Open/Deposit/Withdraw)".
           DISPLAY "3 - Transaction Report".
           DISPLAY "4 - Customer Information".
           DISPLAY "9 - Exit".
           DISPLAY "Enter your choice: ".
           ACCEPT WS-CHOICE.

       HANDLE-CHOICE.
           EVALUATE WS-CHOICE
               WHEN 1
                   PERFORM CALL-CUSTREG
               WHEN 2
                   PERFORM CALL-ACCTMGMT
               WHEN 3
                   PERFORM CALL-TXNREPT
               WHEN 4
                   PERFORM CALL-CUSTINFO
               WHEN 9
                   CONTINUE
               WHEN OTHER
                   DISPLAY "Invalid option. Try again."
           END-EVALUATE.

       CALL-CUSTREG.
           CALL "CUSTREG"
           PERFORM PRESS-ENTER
           DISPLAY WS-CLEAR NO ADVANCING.

       CALL-ACCTMGMT.
           CALL "ACCTMGMT"
           PERFORM PRESS-ENTER
           DISPLAY WS-CLEAR NO ADVANCING.

       CALL-TXNREPT.
           CALL "TXNREPT"
           PERFORM PRESS-ENTER
           DISPLAY WS-CLEAR NO ADVANCING.
           
       CALL-CUSTINFO.
           CALL "CUSTINFO"
           PERFORM PRESS-ENTER
           DISPLAY WS-CLEAR NO ADVANCING.

       PRESS-ENTER.
           DISPLAY "Press ENTER to return to the menu...".
           ACCEPT WS-DUMMY.
