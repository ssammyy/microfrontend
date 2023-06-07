import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StandardLevySuspensionComponent } from './standard-levy-suspension.component';

describe('StandardLevySuspensionComponent', () => {
  let component: StandardLevySuspensionComponent;
  let fixture: ComponentFixture<StandardLevySuspensionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StandardLevySuspensionComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StandardLevySuspensionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
