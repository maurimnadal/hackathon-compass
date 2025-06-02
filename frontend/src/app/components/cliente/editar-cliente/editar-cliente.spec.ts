import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditarCliente } from './editar-cliente';

describe('EditarCliente', () => {
  let component: EditarCliente;
  let fixture: ComponentFixture<EditarCliente>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EditarCliente]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EditarCliente);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
