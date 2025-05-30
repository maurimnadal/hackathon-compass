import { Component, OnInit } from '@angular/core';
import { ReactiveFormsModule, FormGroup, FormBuilder, Validators, AbstractControl, ValidatorFn } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

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

  constructor(private fb: FormBuilder) {}

  ngOnInit() {
    this.clienteForm = this.fb.group({
      nome: ['', [Validators.required, Validators.minLength(1), this.noNumbersValidator()]],
      email: ['', [Validators.required, Validators.email]],
      dataNascimento: ['', [Validators.required, Validators.pattern(/^\d{8}$/)]]
    });
  }

  onSubmit() {
    if (this.clienteForm.valid) {
      // Simulando o registro e geração de ID
      this.clienteId = this.gerarId();
      this.registroSucesso = true;
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
  }

  // Validador personalizado para não permitir números
  noNumbersValidator(): ValidatorFn {
    return (control: AbstractControl): {[key: string]: any} | null => {
      const hasNumbers = /[0-9]/.test(control.value);
      return hasNumbers ? {'containsNumbers': {value: control.value}} : null;
    };
  }

  private gerarId(): string {
    // Simulando a geração de um ID com 5 dígitos
    return String(Math.floor(10000 + Math.random() * 90000)).padStart(5, '0');
  }
}