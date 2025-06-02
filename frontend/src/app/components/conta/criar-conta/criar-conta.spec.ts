import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { CriarContaComponent } from './criar-conta';
import { By } from '@angular/platform-browser';

describe('CriarContaComponent', () => {
  let component: CriarContaComponent;
  let fixture: ComponentFixture<CriarContaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CriarContaComponent],
      imports: [FormsModule]
    }).compileComponents();

    fixture = TestBed.createComponent(CriarContaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('deve criar o componente', () => {
    expect(component).toBeTruthy();
  });

  it('deve exibir mensagem após submissão', () => {
    component.clienteId = '123';
    component.tipoConta = 'corrente';
    component.onSubmit();
    fixture.detectChanges();

    const mensagem = fixture.debugElement.query(By.css('p'));
    expect(mensagem.nativeElement.textContent).toContain('criada com sucesso');
  });

  it('deve limpar os campos após submissão', () => {
    component.clienteId = '999';
    component.tipoConta = 'poupanca';
    component.onSubmit();

    expect(component.clienteId).toBe('');
    expect(component.tipoConta).toBe('');
  });
});
