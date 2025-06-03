import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Account } from '../../models/account/account';

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
  postAccount(account: Account): Observable<Account> {
    return this.http.post<Account>(`${this.apiUrl}accounts`, account);
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
  getTransactions(): Observable<Account[]> {
    return this.http.get<Account[]>(`${this.apiUrl}accounts/reports/transactions`);
  }
}
