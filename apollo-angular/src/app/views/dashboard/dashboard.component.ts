import {Component, OnInit} from '@angular/core';
import {Observable} from "rxjs";
import {select, Store} from "@ngrx/store";
import {loadLogout, selectGetNames} from "../../core/store";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styles: []
})
export class DashboardComponent implements OnInit {
  fullName$!: Observable<string>;

  constructor(
    private store$: Store<any>
  ) {
  }

  ngOnInit(): void {
    this.fullName$ = this.store$.pipe(select(selectGetNames));
    this.getBreadcrumbs();
  }

  onClickLogout() {
    this.store$.dispatch(loadLogout({loginUrl: 'login'}));
  }

  public getBreadcrumbs(): string {
    const here = location.href.split('/').slice(3);
    // console.log(here)
    const parts = [{"text": 'Home', "link": '/'}];
    // console.log(parts)

    for (let i = 0; i < here.length; i++) {
      const part = here[i];
      // console.log(part)
      const text = decodeURIComponent(part).split('.')[0];
      // console.log(text)
      const link = '/' + here.slice(0, i + 1).join('/');
      // console.log(link)
      parts.push({"text": text, "link": link});
      // console.log(parts)
    }
    return parts.map((part) => {
      const breadCrumb = "<a href=\"" + part.link + "\" class='bredcrumb'>" + part.text + "</a>"
      return breadCrumb
    }).join('/')
  }
}
