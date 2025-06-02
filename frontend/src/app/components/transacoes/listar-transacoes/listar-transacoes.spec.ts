import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListarTransacoes } from './listar-transacoes';

describe('ListarTransacoes', () => {
  let component: ListarTransacoes;
  let fixture: ComponentFixture<ListarTransacoes>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ListarTransacoes]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ListarTransacoes);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
