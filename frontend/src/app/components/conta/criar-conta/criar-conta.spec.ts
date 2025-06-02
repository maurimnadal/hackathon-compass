import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { CriarContaComponent } from './criar-conta';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { By } from '@angular/platform-browser';

describe('CriarContaComponent', () => {
  let component: CriarContaComponent;
  let fixture: ComponentFixture<CriarContaComponent>;
  let httpMock: HttpTestingController;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FormsModule, HttpClientTestingModule, CriarContaComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(CriarContaComponent);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);
    fixture.detectChanges();
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('deve criar o componente', () => {
    expect(component).toBeTruthy();
  });

  it('não deve submeter se campos estiverem vazios', () => {
    component.customer_id = null;
    component.type = '';
    component.onSubmit();
    expect(component.mensagem).toBe('Preencha todos os campos.');
  });

  it('deve enviar POST e mostrar sucesso', () => {
    component.customer_id = 123;
    component.type = 'SAVINGS';

    component.onSubmit();

    const req = httpMock.expectOne('URL_DA_SUA_API');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual({
      customer_id: 123,
      type: 'SAVINGS'
    });

    req.flush({}); // simula resposta vazia sucesso

    expect(component.mensagem).toBe('Conta criada com sucesso!');
    expect(component.customer_id).toBeNull();
    expect(component.type).toBe('');
  });

  it('deve mostrar erro se falhar o POST', () => {
    component.customer_id = 123;
    component.type = 'CHECKING';

    component.onSubmit();

    const req = httpMock.expectOne('URL_DA_SUA_API');
    req.error(new ErrorEvent('Erro de rede'));

    expect(component.mensagem).toBe('Erro ao criar conta.');
  });
});
