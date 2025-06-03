import { Routes } from '@angular/router';
import { LayoutComponent } from './components/layout/layout.component';
import { Home } from './components/home/home';
import { RegistrarCliente } from './components/cliente/registrar-cliente/registrar-cliente';
import { EditarCliente } from './components/cliente/editar-cliente/editar-cliente';
import { ListarCliente } from './components/cliente/listar-cliente/listar-cliente';
import { CriarContaComponent } from './components/conta/criar-conta/criar-conta';
import { Depositar } from './components/depositar-sacar/depositar/depositar';
import { Sacar } from './components/depositar-sacar/sacar/sacar';
import { ListarTransacoes } from './components/transacoes/listar-transacoes/listar-transacoes';

export const routes: Routes = [
  {
    path: '',
    component: LayoutComponent,
    children: [
      { path: '', component: Home },
      { path: 'registrar-cliente', component: RegistrarCliente },
      { path: 'editar-cliente', component: EditarCliente },
      { path: 'listar-cliente', component: ListarCliente },
      { path: 'criar-conta', component: CriarContaComponent },
      { path: 'depositar', component: Depositar },
      { path: 'sacar', component: Sacar },
      { path: 'listar-transacoes', component: ListarTransacoes }
    ]
  }
];
