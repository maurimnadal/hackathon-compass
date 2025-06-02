import { Routes } from '@angular/router';

import { Depositar } from './components/depositar-sacar/depositar/depositar';
import { Sacar } from './components/depositar-sacar/sacar/sacar';

import { ListarTransacoes } from './components/transacoes/listar-transacoes/listar-transacoes';
import { RegistrarCliente } from './components/cliente/registrar-cliente/registrar-cliente';
import { ListarCliente } from './components/cliente/listar-cliente/listar-cliente';
import { EditarCliente } from './components/cliente/editar-cliente/editar-cliente';

export const routes: Routes = [
    { path : "registrar-cliente", component : RegistrarCliente},
    { path: 'listar-cliente', component: ListarCliente },
    { path: 'listar-transacoes', component: ListarTransacoes },
    { path : "editar-cliente", component : EditarCliente},
    { path: 'depositar', component: Depositar },
  { path: 'sacar', component: Sacar }

];


