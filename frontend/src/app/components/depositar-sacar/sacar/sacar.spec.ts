import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Sacar } from './sacar';

describe('Sacar', () => {
  let component: Sacar;
  let fixture: ComponentFixture<Sacar>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Sacar]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Sacar);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
