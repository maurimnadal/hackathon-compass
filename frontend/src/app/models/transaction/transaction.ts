export interface TransactionDetail {
    transactionId: number; 
    accountId: number;
    type: 'DEPOSIT' | 'WITHDRAWAL';
    amount: number;
    date: Date;
}

export interface Transaction {
    customerId: number;
    customerName: string;   
    startDate: string;
    endDate: string;
    transactions: TransactionDetail[];
}