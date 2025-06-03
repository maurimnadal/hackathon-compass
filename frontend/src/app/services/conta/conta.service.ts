import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Conta } from '../../models/conta/conta';

@Injectable({
  providedIn: 'root',
})
export class ContaService {
  private apiUrl = 'http://localhost:8080/api/';

  constructor(private http: HttpClient) {}

  //listar as contas de um cliente
  getAccount(): Observable<Conta[]> {
    return this.http.get<Conta[]>(`${this.apiUrl}accounts/list/`);
  }

  //adicionar uma conta
  postAccount(conta: Conta): Observable<Conta> {
    return this.http.post<Conta>(`${this.apiUrl}accounts`, conta);
  }

  //faz um depósito em uma conta
  postDeposit(amount: number, account_id: number): Observable<Conta> {
    const body = { amount: amount };
    return this.http.post<Conta>(
      `${this.apiUrl}accounts/${account_id}/deposit`,
      body
    );
  }

  //faz um saque em uma conta
  postWithdraw(amount: number, account_id: number): Observable<Conta> {
    const body = { amount: amount };
    return this.http.post<Conta>(
      `${this.apiUrl}accounts/${account_id}/withdraw`,
      body
    );
  }

  //retorna a lista de transações de uma conta
  getTransactions(
    userId: string,
    dataInicio: string,
    dataFim: string
  ): Observable<Conta[]> {
    return this.http.get<Conta[]>(
      `${this.apiUrl}accounts/reports/transactions`,
      {
        params: { userId, dataInicio, dataFim },
      }
    );
  }
}
