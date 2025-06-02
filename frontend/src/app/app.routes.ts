import { Routes } from '@angular/router';
import { RegistrarCliente } from './components/cliente/registrar-cliente/registrar-cliente';
import { ListarCliente } from './components/cliente/listar-cliente/listar-cliente';

export const routes: Routes = [
    { path : "registrar-cliente", component : RegistrarCliente},
    { path: 'listar-cliente', component: ListarCliente },
];

