import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Account } from '../../models/account/account';
import { Customer } from '../../models/customer/customer';
import { Transaction } from '../../models/transaction/transaction';

@Injectable({
  providedIn: 'root'
})
export class AccountService {
  private apiUrl = 'http://localhost:8080/api/';

  constructor(private http: HttpClient) { }

  //listar as Accounts de um cliente
  getAccount(account_id: number): Observable<Account[]> {
    return this.http.get<Account[]>(`${this.apiUrl}accounts/${account_id}`);
  }

  //adicionar uma Account
  postAccount(customerId: number, account: Omit<Account, "id" | "balance">): Observable<Omit<Account, "id" | "balance">> {
    // Enviando customerId no corpo da requisição junto com a conta
    const requestBody = {
      customerId: customerId,
      accountType: account.accountType
    };
    return this.http.post<Omit<Account, "id" | "balance">>(`${this.apiUrl}accounts`, requestBody);
  }

  //faz um depósito em uma Account
  postDeposit(amount: number, account_id: number): Observable<Account> {
    const body = { "amount": amount };
    return this.http.post<Account>(`${this.apiUrl}accounts/${account_id}/deposit`, body);
  }

  //faz um saque em uma Account
  postWithdraw(amount: number, account_id: number): Observable<Account> {
    const body = { "amount": amount };
    return this.http.post<Account>(`${this.apiUrl}accounts/${account_id}/withdraw`, body);
  }

  //retorna a lista de transações de uma Account
  getTransactions(params: { customerId: number, startDate: Date, endDate: Date }): Observable<Transaction> {
    const formatDate = (date: Date) =>
      `${date.getFullYear()}${(date.getMonth() + 1).toString().padStart(2, '0')}${date.getDate().toString().padStart(2, '0')}`;

    const httpParams = {
      customerId: params.customerId.toString(),
      startDate: formatDate(params.startDate),
      endDate: formatDate(params.endDate)
    };

    return this.http.get<Transaction>(
      `${this.apiUrl}transactions/report/${params.customerId}`,
      { params: httpParams }
    );
  }

  getCustomerAccounts(customerId: number): Observable<Customer> {
    return this.http.get<Customer>(`${this.apiUrl}accounts/customer/${customerId}`);
  }
}
