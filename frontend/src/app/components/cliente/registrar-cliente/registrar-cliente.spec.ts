import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegistrarCliente } from './registrar-cliente';

describe('RegistrarCliente', () => {
  let component: RegistrarCliente;
  let fixture: ComponentFixture<RegistrarCliente>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RegistrarCliente]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RegistrarCliente);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
