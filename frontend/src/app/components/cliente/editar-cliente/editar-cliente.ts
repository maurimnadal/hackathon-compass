import { Component, OnInit } from '@angular/core';
import { ReactiveFormsModule, FormGroup, FormBuilder, Validators, AbstractControl, ValidatorFn } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { ClienteService } from '../../../services/cliente/cliente.service';
import { Cliente } from '../../../models/cliente/cliente';

@Component({
  selector: 'app-editar-cliente',
  standalone: true,
  imports: [
    ReactiveFormsModule, 
    CommonModule, 
    MatCardModule, 
    MatFormFieldModule, 
    MatInputModule, 
    MatButtonModule,
    MatIconModule
  ],
  templateUrl: './editar-cliente.html',
  styleUrl: './editar-cliente.css'
})
export class EditarCliente implements OnInit {
  buscaForm!: FormGroup;
  editarForm!: FormGroup;
  
  clienteAtual: Cliente | null = null;
  
  clienteEncontrado = false;
  atualizacaoSucesso = false;
  idNaoEncontrado = false;
  errorMessage = '';
  isLoading = false;

  constructor(
    private fb: FormBuilder,
    private clienteService: ClienteService
  ) {}

  ngOnInit() {
    this.buscaForm = this.fb.group({
      id: ['', [Validators.required, Validators.pattern(/^\d+$/)]]
    });

    this.editarForm = this.fb.group({
      nome: ['', [Validators.required, Validators.minLength(1), this.noNumbersValidator()]],
      email: ['', [Validators.required, Validators.email]],
      data_nascimento: ['', [
        Validators.required, 
        this.onlyNumbersValidator(),
        Validators.pattern(/^\d{8}$/)
      ]]
    });
  }

  buscarCliente() {
    if (this.buscaForm.invalid) {
      this.buscaForm.get('id')?.markAsTouched();
      return;
    }

    const id = this.buscaForm.get('id')?.value;
    this.isLoading = true;
    this.errorMessage = '';
    
    this.clienteService.getClient(id).subscribe({
      next: (cliente) => {
        this.clienteAtual = cliente;
        
        // Preenchendo o formulário de edição com os valores atuais
        this.editarForm.patchValue({
          nome: cliente.nome,
          email: cliente.email,
          data_nascimento: cliente.data_nascimento
        });
        
        this.clienteEncontrado = true;
        this.idNaoEncontrado = false;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Erro ao buscar cliente:', err);
        this.idNaoEncontrado = true;
        this.clienteEncontrado = false;
        this.isLoading = false;
        
        if (err.status === 404) {
          this.errorMessage = 'Cliente não encontrado com este ID.';
        } else {
          this.errorMessage = 'Ocorreu um erro ao buscar o cliente. Por favor, tente novamente.';
        }
      }
    });
  }

  onSubmit() {
    if (this.editarForm.invalid) {
      // Marca todos os campos como tocados para mostrar os erros
      Object.keys(this.editarForm.controls).forEach(key => {
        const control = this.editarForm.get(key);
        control?.markAsTouched();
      });
      return;
    }
    
    const id = this.buscaForm.get('id')?.value;
    this.isLoading = true;
    this.errorMessage = '';
    
    const clienteAtualizado: Omit<Cliente, "contas" > = {
      id_cliente: id,
      nome: this.editarForm.value.nome,
      email: this.editarForm.value.email,
      data_nascimento: this.editarForm.value.data_nascimento
    };
    
    this.clienteService.putClient(clienteAtualizado).subscribe({
      next: () => {
        this.atualizacaoSucesso = true;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Erro ao atualizar cliente:', err);
        this.errorMessage = 'Ocorreu um erro ao atualizar o cliente. Por favor, tente novamente.';
        this.isLoading = false;
      }
    });
  }
  
  resetForm() {
    this.buscaForm.reset();
    this.editarForm.reset();
    this.clienteAtual = null;
    this.clienteEncontrado = false;
    this.atualizacaoSucesso = false;
    this.idNaoEncontrado = false;
    this.errorMessage = '';
  }

  // Validador personalizado para não permitir números
  noNumbersValidator(): ValidatorFn {
    return (control: AbstractControl): {[key: string]: any} | null => {
      const hasNumbers = /[0-9]/.test(control.value);
      return hasNumbers ? {'containsNumbers': {value: control.value}} : null;
    };
  }
  
  // Validador personalizado para permitir apenas números
  onlyNumbersValidator(): ValidatorFn {
    return (control: AbstractControl): {[key: string]: any} | null => {
      if (!control.value) {
        return null; // Não validar campo vazio
      }
      const hasNonNumbers = /[^0-9]/.test(control.value);
      return hasNonNumbers ? {'onlyNumbers': {value: control.value}} : null;
    };
  }
}
