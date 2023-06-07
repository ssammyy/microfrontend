import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ForeignCorsComponent} from './foreign-cors.component';

describe('ForeignCorsComponent', () => {
  let component: ForeignCorsComponent;
  let fixture: ComponentFixture<ForeignCorsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ForeignCorsComponent]
    })
        .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ForeignCorsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
