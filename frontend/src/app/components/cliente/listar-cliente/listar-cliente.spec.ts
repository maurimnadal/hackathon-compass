import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListarCliente } from './listar-cliente';

describe('ListarCliente', () => {
  let component: ListarCliente;
  let fixture: ComponentFixture<ListarCliente>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ListarCliente]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ListarCliente);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
