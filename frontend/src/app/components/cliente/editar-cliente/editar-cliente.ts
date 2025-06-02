import { Component, OnInit } from '@angular/core';
import { ReactiveFormsModule, FormGroup, FormBuilder, Validators, AbstractControl, ValidatorFn } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

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
  
  clienteAtual = {
    nome: '',
    email: '',
    dataNascimento: ''
  };
  
  clienteEncontrado = false;
  atualizacaoSucesso = false;
  idNaoEncontrado = false;

  constructor(private fb: FormBuilder) {}

  ngOnInit() {
    this.buscaForm = this.fb.group({
      id: ['', [Validators.required, Validators.pattern(/^\d+$/)]]
    });

    this.editarForm = this.fb.group({
      nome: ['', [Validators.required, Validators.minLength(1), this.noNumbersValidator()]],
      email: ['', [Validators.required, Validators.email]],
      dataNascimento: ['', [
        Validators.required, 
        this.onlyNumbersValidator(),  // Verifica se contém apenas números
        Validators.pattern(/^\d{8}$/)  // Verifica se tem exatamente 8 dígitos
      ]]
    });
  }

  buscarCliente() {
    if (this.buscaForm.invalid) {
      this.buscaForm.get('id')?.markAsTouched();
      return;
    }

    const id = this.buscaForm.get('id')?.value;
    
    // Simulando busca de cliente
    console.log('Buscando cliente com ID:', id);
    
    // Simulando verificação de ID existente (para demonstração, vamos considerar que o ID 00002 existe)
    if (id === '00002') {
      // Simulando dados retornados do backend
      this.clienteAtual = {
        nome: 'sla',
        email: 'sla@gmail.com',
        dataNascimento: '20051214'
      };
      
      // Preenchendo o formulário de edição com os valores atuais
      // this.editarForm.patchValue({
      //   nome: this.clienteAtual.nome,
      //   email: this.clienteAtual.email,
      //   dataNascimento: this.clienteAtual.dataNascimento
      // });
      
      this.clienteEncontrado = true;
      this.idNaoEncontrado = false;
    } else {
      this.idNaoEncontrado = true;
      this.clienteEncontrado = false;
    }
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
    
    // Simulando atualização de cliente
    const clienteAtualizado = {
      id: this.buscaForm.get('id')?.value,
      ...this.editarForm.value
    };
    
    console.log('Atualizando cliente:', clienteAtualizado);
    
    // Simulando uma atualização bem-sucedida
    this.atualizacaoSucesso = true;
  }
  
  resetForm() {
    this.buscaForm.reset();
    this.editarForm.reset();
    this.clienteAtual = {
      nome: '',
      email: '',
      dataNascimento: ''
    };
    this.clienteEncontrado = false;
    this.atualizacaoSucesso = false;
    this.idNaoEncontrado = false;
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