# COBOL Banking System

## Overview

This is a simple COBOL banking system that provides basic banking functionality through a terminal-based interface. The system is containerized using Docker for easy deployment and execution.

## System Components

The banking system consists of several COBOL modules:

- **Main Menu (MAINMENU.cbl)**: The central interface that allows users to navigate between system functionalities.
- **Customer Registration (CUSTREG.cbl)**: Manages customer registration and profile updates.
- **Account Management (ACCTMGMT.cbl)**: Handles account creation, deposits, and withdrawals.
- **Transaction Reports (TXNREPT.cbl)**: Generates transaction reports for a customer within a date range.
- **Customer Information (CUSTINFO.cbl)**: Displays detailed customer information and associated accounts.

## Data Storage

The system uses three sequential data files to store information:

- **CUSTOMER.dat**: Stores customer records (ID, name, email, birthdate)
- **ACCOUNT.dat**: Stores account information (ID, customer ID, type, balance)
- **TRANSACT.dat**: Records all transactions (ID, account ID, type, amount, date, time)

**File Format:**  
> - Fixed-length records, fields separated by spaces.  
> - All data is handled as formatted strings by COBOL.

## Running the System with Docker

To run the COBOL banking system:

1. Build the Docker image:
   ```
   docker build -t cobol-banking-system .
   ```

2. Run the container in detached interactive mode:
   ```
   docker run -dit --name cobol-session cobol-banking-system
   ```

3. Attach to the running container:
   ```
   docker attach cobol-session
   ```

4. To detach from the container without killing it:
   ```
   Ctrl + P  then  Ctrl + Q
   ```

## ⚠️ Warning

**Data Loss Risk**: All data is stored inside the container. If the container is killed or removed, all banking data (customers, accounts, transactions) will be permanently lost.
Make sure to properly detach from the container using the key sequence above rather than exiting with Ctrl+C, which may kill the container. If you choose option 9 in the COBOL main menu, the program will gracefully terminate inside the container and the container will need to be started again.
