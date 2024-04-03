import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

interface Toast {
  message: string;
  type: string;
}

@Injectable({
  providedIn: 'root'
})
export class ToastService {
  private toastSubject = new BehaviorSubject<Toast | null>(null);
  currentToast$: Observable<Toast | null>;

  constructor() {
    this.currentToast$ = this.toastSubject.asObservable();
  }

  showToast(message: string, type: string = 'info') {
    this.toastSubject.next({ message, type });
  }

  dismissToast() {
    this.toastSubject.next(null); // Set next toast to null to hide current toast
  }
}
