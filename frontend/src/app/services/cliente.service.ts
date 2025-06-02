import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Cliente } from '../models/cliente';

@Injectable({
  providedIn: 'root'
})
export class ClienteService {
  private apiUrl = 'http://127.0.0.1:5000/'; 
  
  constructor(private http: HttpClient) {}

  //listar o cliente
  getClient(customer_id: number | null): Observable<Cliente[]> {
    return this.http.get<Cliente[]>(`${this.apiUrl}customers/${customer_id}`);
  }

  
}