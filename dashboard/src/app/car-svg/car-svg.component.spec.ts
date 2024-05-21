import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CarSvgComponent } from './car-svg.component';

describe('CarSvgComponent', () => {
  let component: CarSvgComponent;
  let fixture: ComponentFixture<CarSvgComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CarSvgComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CarSvgComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
