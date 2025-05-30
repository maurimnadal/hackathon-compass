FROM ubuntu:20.04

# Install dependencies
RUN apt update && apt install -y \
    gnucobol \
    nano \
    gcc \
    && rm -rf /var/lib/apt/lists/*

# Create working directory and copy COBOL code into the image
WORKDIR /cobol
COPY . /cobol/

RUN touch CUSTOMER.dat ACCOUNT.dat TRANSACT.dat && \
    cobc -c CUSTREG.cbl && \
    cobc -c ACCTMGMT.cbl && \
    cobc -c TXNREPT.cbl && \
    cobc -c CUSTINFO.cbl && \
    cobc -x MAINMENU.cbl CUSTREG.o ACCTMGMT.o TXNREPT.o CUSTINFO.o -o menu

# Optional entrypoint for terminal access
ENTRYPOINT ["./menu"]