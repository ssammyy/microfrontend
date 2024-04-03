import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-toast',
  templateUrl: './toast.component.html',
  styleUrls: ['./toast.component.css']
})
export class ToastComponent implements OnInit{
  @Input() message : string = ''
  @Input() type : string = 'info'
  timeoutId:any



  ngOnInit() {

    this.timeoutId = setTimeout(() => this.dismissToast(), 5000);
  }
  dismissToast() {
    clearTimeout(this.timeoutId);
  }

}
