import { Routes } from '@angular/router';
import { Depositar } from './components/transacoes/depositar/depositar';
import { Sacar } from './components/transacoes/sacar/sacar';

export const routes: Routes = [
  { path: 'depositar', component: Depositar },
  { path: 'sacar', component: Sacar }];

