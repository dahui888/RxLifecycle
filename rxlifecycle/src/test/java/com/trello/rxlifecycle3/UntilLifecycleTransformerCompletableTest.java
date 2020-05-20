/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.trello.rxlifecycle3;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CancellationException;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.observers.TestObserver;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class UntilLifecycleTransformerCompletableTest {

    PublishSubject<Object> subject;
    Completable completable;
    PublishSubject<String> lifecycle;

    @Before
    public void setup() {
        subject = PublishSubject.create();
        completable = Completable.fromObservable(subject);
        lifecycle = PublishSubject.create();
    }

    @Test
    public void noEvent() {
        TestObserver<Void> testObserver = completable
            .compose(RxLifecycle.bind(lifecycle))
            .test();

        subject.onComplete();

        testObserver.assertComplete();
    }

    @Test
    public void oneEvent() {
        TestObserver<Void> testObserver = completable
            .compose(RxLifecycle.bind(lifecycle))
            .test();

        lifecycle.onNext("stop");
        subject.onComplete();

        testObserver.assertError(CancellationException.class);
    }
}