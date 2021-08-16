import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NwaDiSdtTasksComponent } from './nwa-di-sdt-tasks.component';

describe('NwaDiSdtTasksComponent', () => {
  let component: NwaDiSdtTasksComponent;
  let fixture: ComponentFixture<NwaDiSdtTasksComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NwaDiSdtTasksComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NwaDiSdtTasksComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
