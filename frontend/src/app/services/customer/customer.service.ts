import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Customer } from '../../models/customer/customer';

@Injectable({
  providedIn: 'root'
})
export class CustomerService {
  private apiUrl = 'http://localhost:8080/api/'; 
  
  constructor(private http: HttpClient) {}

  //listar o Customer
  getClient(customer_id: number | null): Observable<Customer> {
    return this.http.get<Customer>(`${this.apiUrl}customers/${customer_id}`);
  }

  //adicionar um Customer
  postClient(customer: Customer | Omit<Customer, "accounts">): Observable<Omit<Customer, "accounts">> {
    return this.http.post<Omit<Customer, "accounts">>(`${this.apiUrl}customers`, customer);
  }

  //atualizar um Customer
  putClient(customer: Omit<Customer, "accounts">): Observable<Omit<Customer, "accounts">> {
    return this.http.put<Omit<Customer, "accounts">>(`${this.apiUrl}customers/${customer.customerId}`, customer);
  }

  
}