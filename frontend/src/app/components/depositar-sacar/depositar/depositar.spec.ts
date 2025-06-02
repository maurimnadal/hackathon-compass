import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Depositar } from './depositar';

describe('Depositar', () => {
  let component: Depositar;
  let fixture: ComponentFixture<Depositar>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Depositar]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Depositar);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
