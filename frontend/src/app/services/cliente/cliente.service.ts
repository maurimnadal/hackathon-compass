import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Cliente } from '../../models/cliente/cliente';

@Injectable({
  providedIn: 'root'
})
export class ClienteService {
  private apiUrl = 'http://localhost:8080/api/'; 
  
  constructor(private http: HttpClient) {}

  //listar o cliente
  getClient(customer_id: number | null): Observable<Cliente> {
    return this.http.get<Cliente>(`${this.apiUrl}customers/${customer_id}`);
  }

  //adicionar um cliente
  postClient(cliente: Cliente | Omit<Cliente, "contas">): Observable<Omit<Cliente, "contas">> {
    return this.http.post<Omit<Cliente, "contas">>(`${this.apiUrl}customers`, cliente);
  }

  //atualizar um cliente
  putClient(cliente: Omit<Cliente, "contas">): Observable<Omit<Cliente, "contas">> {
    return this.http.put<Omit<Cliente, "contas">>(`${this.apiUrl}customers/${cliente.id_cliente}`, cliente);
  }

  
}