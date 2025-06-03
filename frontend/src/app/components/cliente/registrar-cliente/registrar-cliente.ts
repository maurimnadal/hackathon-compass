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
  selector: 'app-registrar-cliente',
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
  templateUrl: './registrar-cliente.html',
  styleUrl: './registrar-cliente.css'
})
export class RegistrarCliente implements OnInit {
  clienteForm!: FormGroup;
  registroSucesso = false;
  clienteId = '';
  errorMessage = '';

  constructor(
    private fb: FormBuilder,
    private clienteService: ClienteService
  ) {}

  ngOnInit() {
    this.clienteForm = this.fb.group({
      nome: ['', [Validators.required, Validators.minLength(1), this.noNumbersValidator()]],
      email: ['', [Validators.required, Validators.email]],
      data_nascimento: ['', [
        Validators.required, 
        this.onlyNumbersValidator(),
        Validators.pattern(/^\d{8}$/)
      ]]
    });
  }

  onSubmit() {
    if (this.clienteForm.valid) {

      const cliente: Omit<Cliente, "contas"> = {
        id_cliente: null,
        nome: this.clienteForm.value.nome,
        email: this.clienteForm.value.email,
        data_nascimento: this.clienteForm.value.data_nascimento
      };
      
      this.clienteService.postClient(cliente).subscribe({
        next: (response) => {
          this.clienteId = response.id_cliente?.toString() || '';
          this.registroSucesso = true;
        },
        error: (err) => {
          console.error('Erro ao registrar cliente:', err);
          this.errorMessage = 'Ocorreu um erro ao registrar o cliente. Por favor, tente novamente.';
        }
      });
    } else {
      // Marca todos os campos como tocados para mostrar os erros
      Object.keys(this.clienteForm.controls).forEach(key => {
        const control = this.clienteForm.get(key);
        control?.markAsTouched();
      });
    }
  }

  resetForm() {
    this.clienteForm.reset();
    this.registroSucesso = false;
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
