import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Conta } from '../../models/conta/conta';

@Injectable({
  providedIn: 'root'
})
export class ContaService {
  private apiUrl = 'http://127.0.0.1:5000/';

  constructor(private http: HttpClient) { }

  //listar as contas de um cliente
  getAccount(): Observable<Conta[]> {
    return this.http.get<Conta[]>(`${this.apiUrl}accounts/list/`);
  }

  //adicionar uma conta
  postAccount(conta: Conta): Observable<Conta> {
    return this.http.post<Conta>(`${this.apiUrl}accounts`, conta);
  }

  //faz um depósito em uma conta
  postDeposit(conta: Conta, account_id: number): Observable<Conta> {
    return this.http.post<Conta>(`${this.apiUrl}accounts/${account_id}/deposit`, conta);
  }

  //faz um saque em uma conta
  postWithdraw(conta: Conta, account_id: number): Observable<Conta> {
    return this.http.post<Conta>(`${this.apiUrl}accounts/${account_id}/withdraw`, conta);
  }

  //retorna a lista de transações de uma conta
  getTransactions(): Observable<Conta[]> {
    return this.http.get<Conta[]>(`${this.apiUrl}accounts/reports/transactions`);
  }
}
