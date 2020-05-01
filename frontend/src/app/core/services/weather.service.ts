import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { IDayWeatherForecastResponse } from 'src/app/shared/models/weather';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class WeatherService {
  private END_POINT_BASE = `${environment.API_URL}/weather`;
  private END_DAILY_FORECAST = `${this.END_POINT_BASE}/forecast/daily`;

  constructor(private http: HttpClient) {}

  getDailyWeatherForecast(): Observable<IDayWeatherForecastResponse[]> {
    return this.http.get<IDayWeatherForecastResponse[]>(this.END_DAILY_FORECAST, {});
  }
}
