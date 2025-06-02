import { Routes } from '@angular/router';
import { Depositar } from './components/depositar-sacar/depositar/depositar';
import { Sacar } from './components/depositar-sacar/sacar/sacar';

export const routes: Routes = [
  { path: 'depositar', component: Depositar },
  { path: 'sacar', component: Sacar }];

